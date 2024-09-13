package com.ct07.ttn.models

data class Address(
    val ward: String?,
    val province: String?,
    val district: String?,
    val road: String?
) {
    override fun hashCode(): Int {
        var result = ward?.hashCode() ?: 0
        result = 31 * result + (province?.hashCode() ?: 0)
        result = 31 * result + (district?.hashCode() ?: 0)
        result = 31 * result + (road?.hashCode() ?: 0)
        return result
    }
}