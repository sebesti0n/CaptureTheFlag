package com.sebesti0n.capturetheflag.util

interface PermissionListener {

    fun   shouldShowRationaleInfo()
    fun   isPermissionGranted(isGranted : Boolean)
}