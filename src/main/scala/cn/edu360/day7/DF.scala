package cn.edu360.day7

import java.sql.DriverManager
import java.util.Properties

import org.apache.spark.sql.{DataFrame, Dataset, Row, SparkSession}

/**
  * Created by JF on 2020/5/4.
  */
object DF {
  val getConn = () => {
    DriverManager.getConnection("jdbc:mysql://localhost:3306/bigdata?characterEncoding=UTF-8", "root", "123456")
  }
  def main(args: Array[String]): Unit = {
    val spark = SparkSession
      .builder()
      .appName("JoinTest")
      .master("local[*]")
      .getOrCreate()

//    spark.read.jdbc()
    val logs: DataFrame = spark.read.format("jdbc").options(
      Map("url" -> "jdbc:mysql://localhost:3306/vhr",
        "driver" -> "com.mysql.jdbc.Driver",
        "dbtable" -> "hr",
        "columnName"->"id",
        "user" -> "root",
        "password" -> "123456")
    ).load()

    val dataFrame: DataFrame = spark.read.jdbc( "jdbc:mysql://localhost:3306/vhr","hr",new Properties())
    dataFrame.select("name").show()
    import spark.implicits._
    val hhh: Dataset[Row] = dataFrame.where($"name"==="系统管理员")
    dataFrame.where(Seq())
    hhh.write.mode("ignore").jdbc("jdbc:mysql://localhost:3306/vhr", "logs1", new Properties())

//    logs.createTempView("t_u")
//    spark.sql("select *from t_u").show()
  }

}
