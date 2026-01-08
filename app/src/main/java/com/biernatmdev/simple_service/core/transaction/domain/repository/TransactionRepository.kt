package com.biernatmdev.simple_service.core.transaction.domain.repository

import com.biernatmdev.simple_service.core.transaction.domain.model.Transaction

interface TransactionRepository {
    suspend fun createTransaction(transaction: Transaction): Result<Unit>
}