package com.kito.feature.gpa.presentation.components
data class Subject(
    val name: String,
    val credit: Int
)

data class SemesterData(
    val semester: Int,
    val branch: String,
    val subjects: List<Subject>
)

val gpaDatabase = listOf(
    SemesterData(
        semester = 1,
        branch = "CSE",
        subjects = listOf(
            Subject("CHEMISTRY", 3),
            Subject("DE & LA", 4),
            Subject("BEE", 2),
            Subject("BETC", 2),
            Subject("ENGLISH", 2),
            Subject("HASS ELECTIVE", 2),
            Subject("CHEMISTRY LAB", 1),
            Subject("WORKSHOP", 1),
            Subject("ENGINEERING LAB", 1),
            Subject("COMMUNICATION LAB", 1),
            Subject("YOGA", 1)
        )
    ),
    SemesterData(
        semester = 2,
        branch = "CSE",
        subjects = listOf(
            Subject("PHYSICS", 3),
            Subject("TC & NM", 4),
            Subject("SCIENCE OF LIVING SYSTEMS", 2),
            Subject("EVS", 2),
            Subject("ENGINEERING ELECTIVE", 2),
            Subject("SCIENCE ELECTIVE", 2),
            Subject("PROGRAMMING LAB", 4),
            Subject("PHYSICS LAB", 1),
            Subject("ENGINEERING DRAWING", 1)
        )
    ),
    SemesterData(
        semester = 3,
        branch = "CSE",
        subjects = listOf(
            Subject("DS", 4),
            Subject("IND 4.0", 2),
            Subject("DSD", 3),
            Subject("AFL", 4),
            Subject("PS", 4),
            Subject("STW", 2),
            Subject("DS LAB", 1),
            Subject("DSD LAB", 1)
        )
    ),
    SemesterData(
        semester = 4,
        branch = "CSE",
        subjects = listOf(
            Subject("OOPJ", 3),
            Subject("OS", 3),
            Subject("COA", 4),
            Subject("DBMS", 3),
            Subject("DM", 4),
            Subject("HASS ELECTIVE", 3),
            Subject("OOPJ LAB", 1),
            Subject("OS LAB", 1),
            Subject("DBMS LAB", 1),
            Subject("VOCATIONAL ELECTIVE", 1)
        )
    ),
    SemesterData(
        semester = 5,
        branch = "CSE",
        subjects = listOf(
            Subject("CN", 3),
            Subject("DAA", 3),
            Subject("SE", 4),
            Subject("PROFESSIONAL ELECTIVE I", 3),
            Subject("PROFESSIONAL ELECTIVE II", 3),
            Subject("HASS ELECTIVE", 3),
            Subject("CN LAB", 1),
            Subject("DAA LAB", 1),
            Subject("K-EXPLORE ELECTIVE", 1)
        )
    ),
    SemesterData(
        semester = 6,
        branch = "CSE",
        subjects = listOf(
            Subject("UHV", 3),
            Subject("AI", 3),
            Subject("ML", 4),
            Subject("CC|SPM|NLP|CV", 3),
            Subject("OPEN ELECTIVE", 3),
            Subject("HASS ELECTIVE", 3),
            Subject("AI LAB", 1),
            Subject("AD LAB", 2),
            Subject("MINI PROJECT", 2)
        )
    ),
    SemesterData(
        semester = 7,
        branch = "CSE",
        subjects = listOf(
            Subject("PROJECT", 5),
            Subject("INTERNSHIP", 2),
            Subject("PROFESSIONAL ELECTIVE", 3),
            Subject("OPEN ELECTIVE", 3),
            Subject("ENGINEERING PROFESSIONAL ELECTIVE", 2),
            Subject("COMPUTING LAB", 2)
        )
    ),
    SemesterData(
        semester = 8,
        branch = "CSE",
        subjects = listOf(
            Subject("PROJECT", 9),
            Subject("MINOR", 3),
            Subject("PROFESSIONAL ELECTIVE", 3),
            Subject("OPEN ELECTIVE", 3)
        )
    ),
    SemesterData(
        semester = 1,
        branch = "CSCE",
        subjects = listOf(
            Subject("CHEMISTRY", 3),
            Subject("DE & LA", 4),
            Subject("BEE", 2),
            Subject("BETC", 2),
            Subject("ENGLISH", 2),
            Subject("HASS ELECTIVE", 2),
            Subject("CHEMISTRY LAB", 1),
            Subject("WORKSHOP", 1),
            Subject("ENGINEERING LAB", 1),
            Subject("COMMUNICATION LAB", 1),
            Subject("YOGA", 1)
        )
    ),
    SemesterData(
        semester = 2,
        branch = "CSCE",
        subjects = listOf(
            Subject("PHYSICS", 3),
            Subject("TC & NM", 4),
            Subject("SCIENCE OF LIVING SYSTEMS", 2),
            Subject("EVS", 2),
            Subject("ENGINEERING ELECTIVE", 2),
            Subject("SCIENCE ELECTIVE", 2),
            Subject("PROGRAMMING LAB", 4),
            Subject("PHYSICS LAB", 1),
            Subject("ENGINEERING DRAWING", 1)
        )
    ),
    SemesterData(
        semester = 3,
        branch = "CSCE",
        subjects = listOf(
            Subject("DS", 4),
            Subject("IND 4.0", 2),
            Subject("DSD", 3),
            Subject("CE", 3),
            Subject("PS", 4),
            Subject("STW", 2),
            Subject("DS LAB", 1),
            Subject("DSD LAB", 1),
            Subject("CE LAB", 1)
        )
    ),
    SemesterData(
        semester = 4,
        branch = "CSCE",
        subjects = listOf(
            Subject("OOPJ", 3),
            Subject("OS", 3),
            Subject("IS", 3),
            Subject("DBMS", 3),
            Subject("DM", 4),
            Subject("ENGINEERING ECONOMICS", 3),
            Subject("OOPJ LAB", 1),
            Subject("OS LAB", 1),
            Subject("DBMS LAB", 1),
            Subject("VOCATIONAL ELECTIVE", 1)
        )
    ),
    SemesterData(
        semester = 5,
        branch = "CSCE",
        subjects = listOf(
            Subject("CN", 3),
            Subject("DAA", 3),
            Subject("SE", 4),
            Subject("PROFESSIONAL ELECTIVE I", 3),
            Subject("PROFESSIONAL ELECTIVE II", 3),
            Subject("HASS ELECTIVE", 3),
            Subject("CN LAB", 1),
            Subject("DAA LAB", 1),
            Subject("K-EXPLORE ELECTIVE", 1)
        )
    ),
    SemesterData(
        semester = 6,
        branch = "CSCE",
        subjects = listOf(
            Subject("UHV", 3),
            Subject("CLOUD COMPUTING", 3),
            Subject("WMC", 3),
            Subject("PROFESSIONAL ELECTIVE", 3),
            Subject("OPEN ELECTIVE", 3),
            Subject("HASS ELECTIVE", 3),
            Subject("WMC LAB", 1),
            Subject("AP LAB", 2),
            Subject("MINI PROJECT", 2)
        )
    ),
    SemesterData(
        semester = 1,
        branch = "CSSE",
        subjects = listOf(
            Subject("CHEMISTRY", 3),
            Subject("DE & LA", 4),
            Subject("BEE", 2),
            Subject("BETC", 2),
            Subject("ENGLISH", 2),
            Subject("HASS ELECTIVE", 2),
            Subject("CHEMISTRY LAB", 1),
            Subject("WORKSHOP", 1),
            Subject("ENGINEERING LAB", 1),
            Subject("COMMUNICATION LAB", 1),
            Subject("YOGA", 1)
        )
    ),
    SemesterData(
        semester = 2,
        branch = "CSSE",
        subjects = listOf(
            Subject("PHYSICS", 3),
            Subject("TC & NM", 4),
            Subject("SCIENCE OF LIVING SYSTEMS", 2),
            Subject("EVS", 2),
            Subject("ENGINEERING ELECTIVE", 2),
            Subject("SCIENCE ELECTIVE", 2),
            Subject("PROGRAMMING LAB", 4),
            Subject("PHYSICS LAB", 1),
            Subject("ENGINEERING DRAWING", 1)
        )
    ),
    SemesterData(
        semester = 3,
        branch = "CSSE",
        subjects = listOf(
            Subject("DS", 4),
            Subject("OOPJ", 3),
            Subject("DSD", 3),
            Subject("COA", 4),
            Subject("PS", 4),
            Subject("STW", 2),
            Subject("DS LAB", 1),
            Subject("DSD LAB", 1),
            Subject("OOPJ LAB", 1)
        )
    ),
    SemesterData(
        semester = 4,
        branch = "CSSE",
        subjects = listOf(
            Subject("IND 4.0", 2),
            Subject("OS", 3),
            Subject("DBMS", 3),
            Subject("DM", 4),
            Subject("ENGINEERING ECONOMICS", 3),
            Subject("PRINCIPLE OF SIGNALS & SYSTEMS", 4),
            Subject("OS LAB", 1),
            Subject("DBMS LAB", 1),
            Subject("VOCATIONAL ELECTIVE", 1)
        )
    ),
    SemesterData(
        semester = 5,
        branch = "CSSE",
        subjects = listOf(
            Subject("CN", 3),
            Subject("DAA", 3),
            Subject("SE", 4),
            Subject("PROFESSIONAL ELECTIVE I", 3),
            Subject("PROFESSIONAL ELECTIVE II", 3),
            Subject("HASS ELECTIVE", 3),
            Subject("CN LAB", 1),
            Subject("DAA LAB", 1),
            Subject("K-EXPLORE ELECTIVE", 1)
        )
    ),
    SemesterData(
        semester = 6,
        branch = "CSSE",
        subjects = listOf(
            Subject("UHV", 3),
            Subject("COMPILERS", 3),
            Subject("ARM & ADV. MICROPROCESSORS", 3),
            Subject("PROFESSIONAL ELECTIVE", 3),
            Subject("OPEN ELECTIVE", 3),
            Subject("HASS ELECTIVE", 3),
            Subject("ARM LAB", 1),
            Subject("AP LAB", 2),
            Subject("MINI PROJECT", 2)
        )
    ),
    SemesterData(
        semester = 1,
        branch = "IT",
        subjects = listOf(
            Subject("CHEMISTRY", 3),
            Subject("DE & LA", 4),
            Subject("BEE", 2),
            Subject("BETC", 2),
            Subject("ENGLISH", 2),
            Subject("HASS ELECTIVE", 2),
            Subject("CHEMISTRY LAB", 1),
            Subject("WORKSHOP", 1),
            Subject("ENGINEERING LAB", 1),
            Subject("COMMUNICATION LAB", 1),
            Subject("YOGA", 1)
        )
    ),
    SemesterData(
        semester = 2,
        branch = "IT",
        subjects = listOf(
            Subject("PHYSICS", 3),
            Subject("TC & NM", 4),
            Subject("SCIENCE OF LIVING SYSTEMS", 2),
            Subject("EVS", 2),
            Subject("ENGINEERING ELECTIVE", 2),
            Subject("SCIENCE ELECTIVE", 2),
            Subject("PROGRAMMING LAB", 4),
            Subject("PHYSICS LAB", 1),
            Subject("ENGINEERING DRAWING", 1)
        )
    ),
    SemesterData(
        semester = 3,
        branch = "IT",
        subjects = listOf(
            Subject("DS", 4),
            Subject("OOPJ", 3),
            Subject("CE", 3),
            Subject("COA", 4),
            Subject("PS", 4),
            Subject("STW", 2),
            Subject("DS LAB", 1),
            Subject("CE LAB", 1),
            Subject("OOPJ LAB", 1)
        )
    ),
    SemesterData(
        semester = 4,
        branch = "IT",
        subjects = listOf(
            Subject("IND 4.0", 2),
            Subject("OS", 3),
            Subject("DBMS", 3),
            Subject("DM", 4),
            Subject("ENGINEERING ECONOMICS", 3),
            Subject("INFORMATION THEORY & CODING", 3),
            Subject("OS LAB", 1),
            Subject("DBMS LAB", 1),
            Subject("VOCATIONAL ELECTIVE", 1)
        )
    ),
    SemesterData(
        semester = 5,
        branch = "IT",
        subjects = listOf(
            Subject("CN", 3),
            Subject("DAA", 3),
            Subject("SE", 4),
            Subject("PROFESSIONAL ELECTIVE I", 3),
            Subject("PROFESSIONAL ELECTIVE II", 3),
            Subject("HASS ELECTIVE", 3),
            Subject("CN LAB", 1),
            Subject("DAA LAB", 1),
            Subject("K-EXPLORE ELECTIVE", 1)
        )
    ),
    SemesterData(
        semester = 6,
        branch = "IT",
        subjects = listOf(
            Subject("UHV", 3),
            Subject("ML", 4),
            Subject("DATA SCIENCE & ANALYTICS", 3),
            Subject("PROFESSIONAL ELECTIVE", 3),
            Subject("OPEN ELECTIVE", 3),
            Subject("HASS ELECTIVE", 3),
            Subject("DA LAB", 1),
            Subject("AP LAB", 2),
            Subject("MINI PROJECT", 2)
        )
    ),
    SemesterData(
        semester = 1,
        branch = "IT",
        subjects = listOf(
            Subject("CHEMISTRY", 3),
            Subject("DE & LA", 4),
            Subject("BEE", 2),
            Subject("BETC", 2),
            Subject("ENGLISH", 2),
            Subject("HASS ELECTIVE", 2),
            Subject("CHEMISTRY LAB", 1),
            Subject("WORKSHOP", 1),
            Subject("ENGINEERING LAB", 1),
            Subject("COMMUNICATION LAB", 1),
            Subject("YOGA", 1)
        )
    ),
    SemesterData(
        semester = 2,
        branch = "IT",
        subjects = listOf(
            Subject("PHYSICS", 3),
            Subject("TC & NM", 4),
            Subject("SCIENCE OF LIVING SYSTEMS", 2),
            Subject("EVS", 2),
            Subject("ENGINEERING ELECTIVE", 2),
            Subject("SCIENCE ELECTIVE", 2),
            Subject("PROGRAMMING LAB", 4),
            Subject("PHYSICS LAB", 1),
            Subject("ENGINEERING DRAWING", 1)
        )
    ),
    SemesterData(
        semester = 3,
        branch = "IT",
        subjects = listOf(
            Subject("DS", 4),
            Subject("OOPJ", 3),
            Subject("CE", 3),
            Subject("COA", 4),
            Subject("PS", 4),
            Subject("STW", 2),
            Subject("DS LAB", 1),
            Subject("CE LAB", 1),
            Subject("OOPJ LAB", 1)
        )
    ),
    SemesterData(
        semester = 4,
        branch = "IT",
        subjects = listOf(
            Subject("IND 4.0", 2),
            Subject("OS", 3),
            Subject("DBMS", 3),
            Subject("DM", 4),
            Subject("ENGINEERING ECONOMICS", 3),
            Subject("INFORMATION THEORY & CODING", 3),
            Subject("OS LAB", 1),
            Subject("DBMS LAB", 1),
            Subject("VOCATIONAL ELECTIVE", 1)
        )
    ),
    SemesterData(
        semester = 5,
        branch = "IT",
        subjects = listOf(
            Subject("CN", 3),
            Subject("DAA", 3),
            Subject("SE", 4),
            Subject("PROFESSIONAL ELECTIVE I", 3),
            Subject("PROFESSIONAL ELECTIVE II", 3),
            Subject("HASS ELECTIVE", 3),
            Subject("CN LAB", 1),
            Subject("DAA LAB", 1),
            Subject("K-EXPLORE ELECTIVE", 1)
        )
    ),
    SemesterData(
        semester = 6,
        branch = "IT",
        subjects = listOf(
            Subject("UHV", 3),
            Subject("ML", 4),
            Subject("DATA SCIENCE & ANALYTICS", 3),
            Subject("PROFESSIONAL ELECTIVE", 3),
            Subject("OPEN ELECTIVE", 3),
            Subject("HASS ELECTIVE", 3),
            Subject("DA LAB", 1),
            Subject("AP LAB", 2),
            Subject("MINI PROJECT", 2)
        )
    ),
    SemesterData(
        semester = 1,
        branch = "ECS",
        subjects = listOf(
            Subject("CHEMISTRY", 3),
            Subject("DE & LA", 4),
            Subject("BEE", 2),
            Subject("BETC", 2),
            Subject("ENGLISH", 2),
            Subject("HASS ELECTIVE", 2),
            Subject("CHEMISTRY LAB", 1),
            Subject("WORKSHOP", 1),
            Subject("ENGINEERING LAB", 1),
            Subject("COMMUNICATION LAB", 1),
            Subject("YOGA", 1)
        )
    ),
    SemesterData(
        semester = 2,
        branch = "ECS",
        subjects = listOf(
            Subject("PHYSICS", 3),
            Subject("TC & NM", 4),
            Subject("SCIENCE OF LIVING SYSTEMS", 2),
            Subject("EVS", 2),
            Subject("ENGINEERING ELECTIVE", 2),
            Subject("SCIENCE ELECTIVE", 2),
            Subject("PROGRAMMING LAB", 4),
            Subject("PHYSICS LAB", 1),
            Subject("ENGINEERING DRAWING", 1)
        )
    ),
    SemesterData(
        semester = 3,
        branch = "ECS",
        subjects = listOf(
            Subject("PS", 4),
            Subject("SIGNAL & SYSTEM", 3),
            Subject("EC", 4),
            Subject("DS", 4),
            Subject("CSA", 3),
            Subject("STW", 2),
            Subject("DS LAB", 1),
            Subject("SP LAB", 1),
            Subject("VOCATIONAL ELECTIVE", 1)
        )
    ),
    SemesterData(
        semester = 4,
        branch = "ECS",
        subjects = listOf(
            Subject("IND 4.0", 2),
            Subject("OS", 3),
            Subject("DBMS", 3),
            Subject("DM", 4),
            Subject("HASS ELECTIVE", 3),
            Subject("CE", 3),
            Subject("EC LAB", 2),
            Subject("DBMS LAB", 1),
            Subject("PROGRAMMING LAB", 2)
        )
    ),
    SemesterData(
        semester = 5,
        branch = "ECS",
        subjects = listOf(
            Subject("MES", 3),
            Subject("DAA", 3),
            Subject("UHV", 3),
            Subject("PROFESSIONAL ELECTIVE I", 3),
            Subject("PROFESSIONAL ELECTIVE II", 3),
            Subject("HASS ELECTIVE", 3),
            Subject("CE LAB", 1),
            Subject("DAA LAB", 1),
            Subject("MES LAB", 1),
            Subject("K-EXPLORE ELECTIVE", 1),
            Subject("ELECTRONICS PROD. DEV.", 3)
        )
    ),
    SemesterData(
        semester = 6,
        branch = "ECS",
        subjects = listOf(
            Subject("SE", 4),
            Subject("CCM|SDN|A & AP|WSN", 3),
            Subject("CN", 3),
            Subject("VLSI", 3),
            Subject("OPEN ELECTIVE", 3),
            Subject("ENGINEERING ECONOMICS", 3),
            Subject("DA LAB", 1),
            Subject("AP LAB", 2),
            Subject("MINI PROJECT", 2)
        )
    )
)