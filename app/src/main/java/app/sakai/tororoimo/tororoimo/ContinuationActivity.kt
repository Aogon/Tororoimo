package app.sakai.tororoimo.tororoimo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import io.realm.Realm
import io.realm.RealmResults
import io.realm.Sort
import kotlinx.android.synthetic.main.activity_continuation.*
import java.util.*

class ContinuationActivity : AppCompatActivity() {
    private val realm: Realm by lazy {
        Realm.getDefaultInstance()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_continuation)

        val resultList = readAll()

//        if(resultList.isEmpty()) {
//            createDummyData()
//        }


        val adapter = ResultAdapter(this, resultList, true)

        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

    }

    override fun onDestroy() {
        super.onDestroy()
        realm.close()
    }

//    fun createDummyData() {
//        for (i in 0..10) {
//            create(i)
//        }
//    }

    fun create(resultTextNumber: Int) {
        realm.executeTransaction {
            val result = it.createObject(Results::class.java, UUID.randomUUID().toString())
            result.resultTextNumber = resultTextNumber
        }
    }

    fun readAll(): RealmResults<Results> {
        return realm.where(Results::class.java).findAll().sort("date", Sort.ASCENDING)
    }
}
