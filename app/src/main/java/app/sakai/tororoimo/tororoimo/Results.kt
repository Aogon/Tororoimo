package app.sakai.tororoimo.tororoimo

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import java.util.*

open class Results (
    @PrimaryKey open var id: String = UUID.randomUUID().toString(),
    open var date: Date = Date(System.currentTimeMillis()),
    open var result: String = ""
) : RealmObject()