package com.despaircorp.domain.filter

import com.despaircorp.domain.estate.model.EstateStatus
import com.despaircorp.domain.estate.model.EstateTypeEntity
import com.despaircorp.domain.estate.model.PointOfInterestEntity
import javax.inject.Inject

class EstateSqlQueryBuilderUseCase @Inject constructor(

) {
    fun invoke(
        surfaceMin: String?,
        surfaceMax: String?,
        priceMin: String?,
        priceMax: String?,
        roomNumber: String?,
        forSale: String?,
        sold: String?,
        entryDate: String?,
        estateType: List<EstateTypeEntity>?,
        pointsOfInterest: List<PointOfInterestEntity>?
    ): String {
        val queryBuilder = StringBuilder("SELECT * FROM estate_table WHERE 1=1")
        
        surfaceMin?.takeIf { it.isNotBlank() }?.also { queryBuilder.append(" AND surface >= $it") }
        surfaceMax?.takeIf { it.isNotBlank() }?.also { queryBuilder.append(" AND surface <= $it") }
        
        when {
            !priceMin.isNullOrBlank() && !priceMax.isNullOrBlank() -> {
                queryBuilder.append(" AND price BETWEEN ${priceMin.toInt()} AND ${priceMax.toInt()}")
            }
            
            !priceMin.isNullOrBlank() -> {
                queryBuilder.append(" AND price >= ${priceMin.toInt()}")
            }
            
            !priceMax.isNullOrBlank() -> {
                queryBuilder.append(" AND price <= ${priceMax.toInt()}")
            }
        }
        
        roomNumber?.takeIf { it.isNotBlank() }
            ?.also { queryBuilder.append(" AND roomNumber = $it") }
        
        forSale?.takeIf { it.isNotBlank() }
            ?.also { queryBuilder.append(" AND status = '${EstateStatus.FOR_SALE.name}'") }
        sold?.takeIf { it.isNotBlank() }
            ?.also { queryBuilder.append(" AND status = '${EstateStatus.SOLD.name}'") }
        
        entryDate?.takeIf { it.isNotBlank() }
            ?.also { queryBuilder.append(" AND entryDate = '$it'") }
        
        if (pointsOfInterest?.isNotEmpty() == true) {
            val poiConditions =
                pointsOfInterest.joinToString(separator = " OR ") { "pointOfInterest LIKE '%${it.pointOfInterestEnum.name}%'" }
            queryBuilder.append(" AND ($poiConditions)")
        }
        
        if (estateType?.isNotEmpty() == true) {
            val estateTypeConditions =
                estateType.joinToString(separator = " OR ") { "estateType LIKE '%${it.estateTypeEnum.name}%'" }
            queryBuilder.append(" AND ($estateTypeConditions)")
        }
        
        return queryBuilder.toString()
    }
}