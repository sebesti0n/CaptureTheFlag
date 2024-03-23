package com.tejasdev.repospect.util

interface PermissionListener {

    fun   shouldShowRationaleInfo()
    fun   isPermissionGranted(isGranted : Boolean)
}