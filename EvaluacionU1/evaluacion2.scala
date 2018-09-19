//1 Comenzar spark SparkSession
import org.apache.spark.sql.SparkSession
val spar = SparkSession.builder().getOrCreate()

//2.- Cargar archivo "Netflix_2011_2016.csv"
val df = spark.read.option("header", "true").option("inferSchema","true")csv("Netflix_2011_2016.csv")

//3.- Nombres de las columnas
df.columns
//Imprime: Array[String] = Array(Date, Open, High, Low, Close, Volume, Adj Close)

//4.- Esquema
df.printSchema()

//5.- Imprimir las primeras 5 columnas
val a = df.head(5)
for(x <- a){
  println(x)
}

//6.- Usar .describe() para aprender sobre el dataframe
df.describe().show()

//7.- crear un nuevo dataframe con columna 'HV Ratio'
val df2 = df.select(corr("High","Volume").as("HV Ratio"))
df2.show()

//8.- El dia que tuvo Peak High en High
val ye = df.withColumn("Years", year(df("Date")))
val peak = ye.groupBy("Years").max()
peak.select($"Years", $"max(High)").show()

//9.- Mean of Close
df.select(mean("Close")).show()
//La columna representa la cantidad de cierre de la bolsa de valores

//10.- Max y min de Volume
df.select(max("Volume"),min("Volume")).show()

//11.- Con sintaxis scala/spark:
import spark.implicits._

  //a.- Cierre inferior a 600
  df.filter($"Close" < 600).show()

  //b.- Porcentaje de tiempo Alto mayor de 500
  (df.filter($"High" > 500).count().toDouble / df.count) * 100

  //c.- Correlación de Pearson entre Alto y Volume
  df.select(corr($"High",$"Volume")).show()

  //d.- Máximo de alto por año
  val years = df.withColumn("Year", year(df("Date")))
  val maxim = years.groupBy("Year").max()
  maxim.select($"Year", $"max(High)").show()

  //e.- Promedio de cierre para cada mes
  val mes = df.withColumn("Month", month(df("Date")))
  val pro = mes.groupBy("Month").avg()
  pro.select($"Month", $"avg(Close)").show()
