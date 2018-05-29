package com.lijieandroid.corelib.glide

import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.module.AppGlideModule

@GlideModule
class GlobalGlideConfig : AppGlideModule() {
    override fun isManifestParsingEnabled() = false
}