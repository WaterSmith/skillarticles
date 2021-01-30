package ru.skillbranch.skillarticles.data.delegates

import ru.skillbranch.skillarticles.data.local.PrefManager
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

class PrefDelegate<T>(private val defaultValue: T) {
    private var storedValue: T? = null

    operator fun provideDelegate(
        thisRef: PrefManager,
        prop: KProperty<*>
    ): ReadWriteProperty<PrefManager, T?> {
        val key:String = prop.name
        return object : ReadWriteProperty<PrefManager, T?>{
            @Suppress("UNCHECKED_CAST")
            override fun getValue(thisRef: PrefManager, property: KProperty<*>): T? {
                if (storedValue == null){
                    storedValue = when(defaultValue){
                        is Int -> thisRef.preferences.getInt(key, defaultValue as Int) as T
                        is Long -> thisRef.preferences.getLong(key, defaultValue as Long) as T
                        is Float -> thisRef.preferences.getFloat(key, defaultValue as Float) as T
                        is String -> thisRef.preferences.getString(key, defaultValue as String) as T
                        is Boolean -> thisRef.preferences.getBoolean(key, defaultValue as Boolean) as T
                        else -> error("This type can not be stored into Preferences")
                    }
                }
                return storedValue
            }

            override fun setValue(thisRef: PrefManager, property: KProperty<*>, value: T?) {
                with(thisRef.preferences.edit()){
                    when(value){
                        is Int -> putInt(key, value).apply()
                        is Long -> putLong(key, defaultValue as Long)
                        is Float -> putFloat(key, defaultValue as Float)
                        is String -> putString(key, defaultValue as String)
                        is Boolean -> putBoolean(key, defaultValue as Boolean)
                        else -> error("Only primitive types can be stored into Preferences")
                    }
                    apply()
                }
                storedValue = value
            }
        }
    }
}