package com.tejasdev.repospect.util

import com.tejasdev.repospect.models.Event

interface EventItemClickListener {
    fun onEventClickListner(event: Event)
}