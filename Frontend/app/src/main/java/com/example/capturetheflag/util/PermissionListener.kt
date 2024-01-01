package com.example.capturetheflag.util

interface PermissionListener {

    fun   shouldShowRationaleInfo()
    fun   isPermissionGranted(isGranted : Boolean)
}