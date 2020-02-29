import org.apache.spark.sql.SQLContext
import org.apache.spark.{SparkConf, SparkContext}
object ReadCSVFile {
  case class Employee(empno:String, ename:String, designation:String, manager:String, hire_date:String, sal:String , deptno:String)
  def main(args : Array[String]): Unit = {
    var conf = new SparkConf().setAppName("Read CSV File").setMaster("local[*]")
    val sc = new SparkContext(conf)
    val sqlContext = new SQLContext(sc)
    import sqlContext.implicits._
  

     val empDF= sqlContext.read.format("csv")
  .option("header", "true")
  .option("inferSchema", "true")
  .load("*.csv")
   empDF.show()

 
     
     sc.stop()
  }
}