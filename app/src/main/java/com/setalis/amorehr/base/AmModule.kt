package com.setalis.amorehr.base

import org.koin.core.module.Module

interface AmModule {

    var loadNeeded: Boolean
    fun getModule(): Module

}