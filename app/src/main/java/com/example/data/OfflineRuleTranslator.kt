package com.example.data

object OfflineRuleTranslator {

    data class LexiconEntry(
        val category: String,
        val emotionalPhrase: String,
        val corporateTranslation: String
    )

    val LEXICON = listOf(
        LexiconEntry(
            category = "Grenzen setzen",
            emotionalPhrase = "Ich habe dir das schon tausendmal gesagt!",
            corporateTranslation = "Wie bereits in meiner vorherigen E-Mail dargelegt..."
        ),
        LexiconEntry(
            category = "Grenzen setzen",
            emotionalPhrase = "Warum liest du deine E-Mails nicht?",
            corporateTranslation = "Bezüglich des aktuellen Sachstands verweise ich auf unsere bisherige Korrespondenz."
        ),
        LexiconEntry(
            category = "Qualität & Kritik",
            emotionalPhrase = "Das ist die dümmste Idee, die ich je gehört habe!",
            corporateTranslation = "Vielen Dank für den Vorschlag. Nach sorgfältiger Prüfung sehen wir im aktuellen Rahmen wesentliches Optimierungspotenzial."
        ),
        LexiconEntry(
            category = "Aufgaben & Verantwortung",
            emotionalPhrase = "Das ist nicht mein Job / Nicht mein Problem!",
            corporateTranslation = "Dafür ziehe ich gerne die entsprechend zuständige Fachabteilung hinzu."
        ),
        LexiconEntry(
            category = "Termine & Fristen",
            emotionalPhrase = "Warum dauert das so verdammt lange? Ihr seid extrem lahm!",
            corporateTranslation = "Um den geplanten Meilenstein zeitnah zu erreichen, bitten wir um eine kurzfristige Statusaktualisierung."
        ),
        LexiconEntry(
            category = "Finanzen & Kosten",
            emotionalPhrase = "Ich werde für diesen Quatsch sicher kein Geld bezahlen!",
            corporateTranslation = "Bezüglich der Abrechnung bitten wir um eine detaillierte Abstimmung der vertraglich vereinbarten Leistungen."
        ),
        LexiconEntry(
            category = "Verlässlichkeit",
            emotionalPhrase = "Ihr seid absolut unzuverlässig und chaotisch!",
            corporateTranslation = "Für die künftige Zusammenarbeit ist die verbindliche Einhaltung der verabschiedeten Zeitpläne von zentraler Bedeutung."
        ),
        LexiconEntry(
            category = "Priorisierung",
            emotionalPhrase = "Hört auf, mich mit so einem Unsinn zu nerven!",
            corporateTranslation = "Wir bitten darum, operative Detailfragen priorisiert auf Teamebene zu klären."
        ),
        LexiconEntry(
            category = "Absagen & Grenzen",
            emotionalPhrase = "Das mache ich ganz sicher nicht!",
            corporateTranslation = "Nach Abwägung der strategischen Prioritäten können wir diesen Ansatz derzeit nicht weiterverfolgen."
        ),
        LexiconEntry(
            category = "Führung & Abgabe",
            emotionalPhrase = "Kümmer dich gefälligst selber darum!",
            corporateTranslation = "Wir bitten Sie, die operative Umsetzung eigenverantwortlich im jeweiligen Zuständigkeitsbereich zu steuern."
        ),
        LexiconEntry(
            category = "Kapazität",
            emotionalPhrase = "Ich habe jetzt wirklich keine Zeit für deinen Kram!",
            corporateTranslation = "Aufgrund aktueller Kapazitätsbindung bitten wir um Terminierung zu einem späteren Zeitpunkt."
        )
    )

    fun translate(inputText: String, toneStyle: String): String {
        val trimmed = inputText.trim()
        if (trimmed.isEmpty()) return ""

        // Process core transformations based on keywords
        val lower = trimmed.lowercase()
        val sentences = mutableListOf<String>()

        if (lower.contains("tausendmal") || lower.contains("schon gesagt") || lower.contains("bereits gesagt") || lower.contains("x-mal")) {
            sentences.add("Wie bereits in meiner vorherigen E-Mail erwähnt, verweise ich bezüglich der Details auf die verabschiedete Dokumentation.")
        }

        if (lower.contains("dümmst") || lower.contains("schwachsinn") || lower.contains("unsinn") || lower.contains("verrückt") || lower.contains("blödsinn")) {
            sentences.add("Nach eingehender Prüfung des vorgelegten Konzeptes sehen wir im aktuellen Rahmen noch erhebliches Überarbeitungspotenzial.")
        }

        if (lower.contains("dauert") || lower.contains("lahm") || lower.contains("langsam") || lower.contains("wann") || lower.contains("fertig")) {
            sentences.add("Um den geplanten Zeitplan einzuhalten, bitten wir um eine verbindliche Rückmeldung zum aktuellen Fertigstellungsgrad.")
        }

        if (lower.contains("bezahlen") || lower.contains("geld") || lower.contains("rechnung") || lower.contains("kosten") || lower.contains("budget")) {
            sentences.add("Bezüglich der finanziellen Rahmenbedingungen bitten wir um eine transparente Aufstellung auf Basis der vereinbarten Konditionen.")
        }

        if (lower.contains("unzuverlässig") || lower.contains("chaos") || lower.contains("Fehler")) {
            sentences.add("Für die reibungslose Fortführung der Zusammenarbeit legen wir großen Wert auf eine hohe Prozessqualität und Verlässlichkeit.")
        }

        // Default structured corporate email if no specific triggers matched
        if (sentences.isEmpty()) {
            val sanitized = trimmed
                .replace("!", ".")
                .replace("?", ".")
                .replace("scheiße", "Herausforderung")
                .replace("mist", "Verzögerung")

            sentences.add("bezüglich Ihrer Rückmeldung möchten wir folgenden Sachverhalt festhalten: $sanitized")
            sentences.add("Wir streben eine konstruktive und lösungsorientierte Abstimmung an.")
        }

        val body = when (toneStyle) {
            "Grenzen aufzeigen" -> {
                "Sehr geehrte Damen und Herren,\n\n" +
                        sentences.joinToString(" ") + "\n\n" +
                        "Wir bitten um Verständnis, dass die einzuhaltenden Vorgaben maßgeblich für den Projekterfolg sind und verbleiben mit der Bitte um Einhaltung des abgestimmten Rahmens.\n\n" +
                        "Mit freundlichen Grüßen"
            }
            "Kurz & Prägnant" -> {
                "Sehr geehrte Damen und Herren,\n\n" +
                        "bezüglich des aktuellen Vorgangs fassen wir die wesentlichen Punkte zusammen:\n\n" +
                        sentences.joinToString("\n• ", prefix = "• ") + "\n\n" +
                        "Mit freundlichen Grüßen"
            }
            "Deeskalierend" -> {
                "Sehr geehrte Damen und Herren,\n\n" +
                        "vielen Dank für Ihren Hinweis. Wir nehmen die Situation sehr ernst. " +
                        sentences.joinToString(" ") + "\n\n" +
                        "Lassen Sie uns zeitnah gemeinsam den optimalen nächsten Schritt abstimmen.\n\n" +
                        "Mit freundlichen Grüßen"
            }
            else -> { // Diplomatisch & Lösungsorientiert
                "Sehr geehrte Damen und Herren,\n\n" +
                        sentences.joinToString(" ") + "\n\n" +
                        "Für Rückfragen sowie die Abstimmung der nächsten Schritte stehen wir Ihnen jederzeit gerne zur Verfügung.\n\n" +
                        "Mit freundlichen Grüßen"
            }
        }

        return body
    }
}
