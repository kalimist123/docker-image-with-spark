import org.apache.spark.{SparkContext, SparkConf}
import org.apache.spark.SparkContext._

object WordCountOrdering extends Ordering[(String, Int)] {

  override def compare(word1: (String, Int), word2: (String, Int)): Int = word2._2 - word1._2

}

object WordCount extends App {

  // We'll run our project in local mode
  val conf = new SparkConf().
    setAppName("word-count").
    setMaster("local[*]").
    set("spark.eventLog.enabled","true")

  // Here we initialize the context with the configuration above
  val sc = new SparkContext(conf)

  // Load the file
  val lines = sc.textFile("pagerank.txt")

  // Take the lines in the file and split the in individual words
  val words = lines.flatMap(line => line.split(" "))

  // Keep only words longer than 4
  val longerThanFour = words.filter(word => word.length > 4)

  // Couple each word with a "weight" of 1
  val weighted = longerThanFour.map(word => word -> 1)

  // Sum the weights to couple words with their counts
  val counts = weighted.reduceByKey(_ + _)

  // Get only the top ten
  val topTen = counts.takeOrdered(10)(WordCountOrdering)

  // Print each item in the RDD
  for ((word, count) <- topTen) {
    println(s"$word -> ${Console.GREEN}$count${Console.RESET}")
  }

  // Halt the context before closing the application
  sc.stop()

} 