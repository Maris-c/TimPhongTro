package com.ct07.ttn.models

import java.io.Serializable

data class District(
    val code: String,
    val code_name: String,
    val full_name: String,
    val full_name_en: String,
    val name: String,
    val name_en: String,
    val province_code: String
): Serializable