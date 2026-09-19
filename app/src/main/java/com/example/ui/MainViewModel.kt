package com.example.ui

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.BuildConfig
import com.example.data.OfflineRuleTranslator
import com.example.data.api.GeminiContent
import com.example.data.api.GeminiGenerationConfig
import com.example.data.api.GeminiPart
import com.example.data.api.GeminiRequest
import com.example.data.api.RetrofitClient
import com.example.data.db.AppDatabase
import com.example.data.db.TranslationEntity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val db = AppDatabase.getDatabase(application)
    private val dao = db.translationDao()

    val historyList: StateFlow<List<TranslationEntity>> = dao.getAllTranslations()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val favoriteList: StateFlow<List<TranslationEntity>> = dao.getFavoriteTranslations()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _inputText = MutableStateFlow("")
    val inputText: StateFlow<String> = _inputText.asStateFlow()

    private val _selectedTone = MutableStateFlow("Grenzen aufzeigen")
    val selectedTone: StateFlow<String> = _selectedTone.asStateFlow()

    private val _translatedEmail = MutableStateFlow("")
    val translatedEmail: StateFlow<String> = _translatedEmail.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    private val _toastMessage = MutableStateFlow<String?>(null)
    val toastMessage: StateFlow<String?> = _toastMessage.asStateFlow()

    val toneOptions = listOf(
        "Grenzen aufzeigen",
        "Diplomatisch",
        "Kurz & Prägnant",
        "Deeskalierend"
    )

    val sampleInputs = listOf(
        "Ich habe dir das schon tausendmal gesagt! Warum liest du deine E-Mails eigentlich nicht?!",
        "Das ist die dümmste Idee, die ich je gehört habe. Völlig verrückt geworden!",
        "Warum ist das Projekt immer noch nicht fertig? Ihr seid extrem lahm und unzuverlässig!",
        "Ich werde für diesen Quatsch garantiert keinen Cent bezahlen. Schickt mir keine Rechnung!"
    )

    fun onInputTextChange(text: String) {
        _inputText.value = text
    }

    fun onToneSelect(tone: String) {
        _selectedTone.value = tone
    }

    fun clearToast() {
        _toastMessage.value = null
    }

    fun translateEmail() {
        val query = _inputText.value.trim()
        if (query.isEmpty()) return

        _isLoading.value = true
        _errorMessage.value = null

        viewModelScope.launch {
            var resultText = ""
            val apiKey = try { BuildConfig.GEMINI_API_KEY } catch (e: Exception) { "" }

            if (!apiKey.isNullOrBlank() && apiKey != "MY_GEMINI_API_KEY") {
                try {
                    val systemPrompt = """
                        Du bist ein Kommunikationsexperte für C-Level-Führungskräfte. Deine einzige Aufgabe ist es, emotionale, wütende oder unprofessionelle Nachrichten in perfekt formulierte, extrem höfliche, aber bestimmte Business-E-Mails zu übersetzen.

                        Regeln für deine Antwort:
                        Erfinde keine neuen Fakten hinzu, sondern nutze nur die Informationen aus dem Input des Nutzers.
                        Der Tonfall muss stets professionell, diplomatisch und lösungsorientiert sein.
                        Wenn der Nutzer extrem wütend ist, nutze subtile, hochprofessionelle "Corporate"-Formulierungen, um Grenzen aufzuzeigen (z. B. "Wie bereits in meiner vorherigen E-Mail erwähnt..." statt "Ich habe dir das schon tausendmal gesagt").
                        Antworte ausschließlich mit dem fertigen E-Mail-Text. Keine Einleitung, keine Erklärungen, nur der Text, den der Nutzer direkt kopieren kann.
                        
                        Nuance: ${_selectedTone.value}
                    """.trimIndent()

                    val request = GeminiRequest(
                        contents = listOf(
                            GeminiContent(
                                parts = listOf(GeminiPart(text = "Eingabenachricht des Nutzers: $query"))
                            )
                        ),
                        systemInstruction = GeminiContent(
                            parts = listOf(GeminiPart(text = systemPrompt))
                        ),
                        generationConfig = GeminiGenerationConfig(temperature = 0.2f)
                    )

                    val response = RetrofitClient.geminiService.generateContent(apiKey, request)
                    val candidateText = response.candidates?.firstOrNull()?.content?.parts?.firstOrNull()?.text
                    if (!candidateText.isNullOrBlank()) {
                        resultText = candidateText.trim()
                    }
                } catch (e: Exception) {
                    Log.e("ExecTranslate", "Gemini API error, using offline fallback", e)
                }
            }

            // Fallback if API returned empty or failed
            if (resultText.isBlank()) {
                resultText = OfflineRuleTranslator.translate(query, _selectedTone.value)
            }

            _translatedEmail.value = resultText
            _isLoading.value = false

            // Save to database
            try {
                dao.insertTranslation(
                    TranslationEntity(
                        originalText = query,
                        translatedEmail = resultText,
                        toneStyle = _selectedTone.value
                    )
                )
            } catch (e: Exception) {
                Log.e("ExecTranslate", "Failed to save translation to DB", e)
            }
        }
    }

    fun toggleFavorite(item: TranslationEntity) {
        viewModelScope.launch {
            dao.updateFavorite(item.id, !item.isFavorite)
        }
    }

    fun deleteTranslation(item: TranslationEntity) {
        viewModelScope.launch {
            dao.deleteTranslation(item)
            _toastMessage.value = "E-Mail aus dem Verlauf gelöscht"
        }
    }

    fun showToast(message: String) {
        _toastMessage.value = message
    }
}
