package com.example.teamkook

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.lang.NumberFormatException
import java.util.*
import kotlin.collections.ArrayList

class FoodClassification(val inputstream1:InputStream, val context: Context):
SQLiteOpenHelper(context,DB_NAME,null,DB_VERSION){

    companion object{
        val DB_VERSION=1
        val DB_NAME="foodclassification.db"
        val TABLE_NAME="classify"
        //col
        val ID="id" //�ڵ��ο��Ǵ� ���̵�
        val NAME="name" //
        val COMMENT="comment" //
        val PEANUT="peanut" //
        val MILK="milk"//
        val EGG="egg" //
        val CRUSTACEAN="crustacean" //
        val FLOUR="flour" //
        val PEACH="peach" //
        val VEGAN="vegan" //
        val BREAKFAST="breakfast"
        val LUNCH="lunch"
        val DINNER="dinner"
        val RAIN="rain"
        val SNOW="snow"
        val HOT="hot"
        val COLD="cold"
        val SPICY="spicy"

    }

    fun readFiles(){

        var readClaasification1:BufferedReader?=null

        try {
            readClaasification1= BufferedReader(InputStreamReader(inputstream1,"EUC-KR"))


            var line1=""
            line1=readClaasification1!!.readLine().toString() //�з��� �� ù��

            while(true){
                line1=readClaasification1!!.readLine().toString()

                var item=line1.split(",")
                try {
                    val food=Food(item[0],item[1],item[2].toInt(),item[3].toInt(),item[4].toInt(),item[5].toInt(),item[6].toInt(),item[7].toInt(),
                        item[8].toInt(),item[9].toInt(),item[10].toInt(),item[11].toInt(),item[12].toInt(),item[13].toInt(),
                        item[14].toInt(),item[15].toInt(),item[16].toInt())
                    if(!register(food)){
                        Log.e("Error : ","�����ͺ��̽� ���� ����")
                    }
                }catch (e:NumberFormatException){
                    Log.i("index error",item[0]+item[1])
                }
                if(line1.contains("�ֲٹ̺���")){
                    return
                }
            }


        }catch (e: IOException){
            Log.e("Error : ",e.toString())
        }

    }

    fun register(item:Food):Boolean{
        val values=ContentValues()
        values.put(NAME,item.name)
        values.put(COMMENT,item.comment)
        values.put(PEANUT,item.peanut)
        values.put(MILK,item.milk)
        values.put(EGG,item.egg)
        values.put(CRUSTACEAN,item.crustacean)
        values.put(FLOUR,item.flour)
        values.put(PEACH,item.peach)
        values.put(BREAKFAST,item.breakfast)
        values.put(LUNCH,item.lunch)
        values.put(DINNER,item.dinner)
        values.put(RAIN,item.rain)
        values.put(SNOW,item.snow)
        values.put(HOT,item.hot)
        values.put(COLD,item.cold)
        values.put(SPICY,item.spicy)
        val db=this.writableDatabase
        if(db.insert(TABLE_NAME,null,values)>0){
            db.close()
            return true
        }
        db.close()
        return false
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val create_table="create table if not exists "+TABLE_NAME+
        " ("+ ID +" integer primary key, "+NAME+" text, "+COMMENT+" text, "+
                PEANUT+" integer, " + MILK+" integer, " + EGG+" integer, " + CRUSTACEAN+" integer, " + FLOUR+" integer, "+
                PEACH+" integer, "+ BREAKFAST+" integer, "+ LUNCH+" integer, "+ DINNER+" integer, "+ RAIN+" integer, "+
                SNOW+" integer, "+ HOT+" integer, "+ COLD+" integer, "+SPICY+" integer)"//
        Log.i("table query ",create_table)
        db?.execSQL(create_table) //sql ���� ����
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
    }

    //���� : FoodClassification ��ü ���� ��
    //�ش� ��ü.recommendRandom() ���� ��� �� �̸��ϰ� Ű����� �����̽��� ���ø��ؼ� ������ ���� ��
    //recommendRandom parameter�� ��õ ���� String���� �־������.
    fun recommendRandom(standard: ArrayList<String>):String?{ //���� �޴� ��õ, �˷��� ������ arraylist�� ���� �Ŀ� ���̺� ���� ����
        // �޴� �̸��� comment ��ȯ
        //standard�� �˷��� �ִ� �׸���̹Ƿ� where������ 0
        var strsql="select * from "+ TABLE_NAME
        if(standard.size>0){
            strsql+=" where "
            for(i in 0 until standard.size-1){
                strsql+= standard[i] +" = \'" + 0 + "\'"+" and "

            }
            strsql+=standard[standard.lastIndex] +" = \'" + 0 + "\'"
        }
        //standard�� ��������� �׳� ��ü �˻���
        val db=this.readableDatabase
        val cursor=db.rawQuery(strsql,null)
        if(cursor.count!=0)
        {
            val rand= Random().nextInt(cursor.count)+1
            cursor.move(rand)
            val name=cursor?.getString(1)
            val comment=cursor?.getString(2)
            cursor.close()
            db.close()
            return comment+" "+name //���ĸ��̶� �ڸ�Ʈ Ű���� ��ȯ
        }
        cursor.close()
        db.close()
        return null
    }

    fun recommendCondition(condition:String, check:Int, standard: ArrayList<String>):String?{
        var strsql="select * from "+ TABLE_NAME + " where "+condition +" = \'" + check + "\'"
        if(standard!=null){
            for(i in 0 until standard.size){
                strsql+= " and "+standard[i] +" = \'" + 0 + "\'"
            }
        }
        //standard�� ��������� �׳� ��ü �˻���
        val db=this.readableDatabase
        val cursor=db.rawQuery(strsql,null)
        if(cursor.count!=0)
        {
            val rand= Random().nextInt(cursor.count)+1
            cursor.move(rand)
            val name=cursor?.getString(1)
            val comment=cursor?.getString(2)
            cursor.close()
            db.close()
            return comment+" "+name
        }
        cursor.close()
        db.close()
        return null
    }
}