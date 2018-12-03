// Databricks notebook source
/*1.- IMPORTACIONES*/
import org.apache.spark.sql.SparkSession
import org.apache.log4j._
import org.apache.spark.ml.clustering.KMeans
import org.apache.spark.ml.clustering.BisectingKMeans
import org.apache.spark.ml.feature.VectorAssembler
import org.apache.spark.ml.linalg.Vectors
import org.apache.spark.sql.functions._

// COMMAND ----------

/*2.- CREACION DE SPARK SESSION*/
Logger.getLogger("org").setLevel(Level.ERROR)
val spark = SparkSession.builder().getOrCreate()

// COMMAND ----------

/*3.- CARGAR EL CSV Y MUESTRA DE DATOS*/
val dataset  = spark.read.format("csv").load("/FileStore/tables/Iris.csv")
dataset.show(20)
dataset.head(1)

// COMMAND ----------

/*4.- LIMPIEZA DE LOS DATOS*/
/*4.1.-
DEBIDO A QUE LOS DATOS ESTAN EN TIPO STRING, SE REQUIERE HACER UNA CONVERSION
A TIPO DOUBLE*/

val doubleConv = udf[Double, String](_.toDouble)
val df_1 = dataset.
withColumn("_c0",doubleConv(dataset("_c0"))).
withColumn("_c1",doubleConv(dataset("_c1"))).
withColumn("_c2",doubleConv(dataset("_c2"))).
withColumn("_c3",doubleConv(dataset("_c3"))).
select($"_c0".as("SepalLengthCm"),$"_c1".as("SepalWidthCm"),$"_c2".as("PetalLengthCm"),$"_c3".as("PetalWidthCm"))

/*4.2.- EXTRAER CARACTERISTICAS (FEATURES)*/
val assembler = (new VectorAssembler().setInputCols(Array("SepalLengthCm","SepalWidthCm", "PetalLengthCm","PetalWidthCm")).setOutputCol("features"))
val df = assembler.transform(df_1).select($"features")
df.show()

// COMMAND ----------

/*5.- CREACION DEL MODELO DE AGRUPACION KMEANS*/
val kmeans = new KMeans().setK(10).setSeed(1L)
val k_model = kmeans.fit(df)

/*PLOT: KMeans*/
display(k_model,df)

/*5.1 RESULTADOS DE KMEANS*/
val WSSE = k_model.computeCost(df)
println(s"Within set sum of Squared Errors = $WSSE")

//MUESTRAN RESULTADOS
println("Cluster Centers: ")
k_model.clusterCenters.foreach(println)

// COMMAND ----------

/*6 CREACION DEL MODELO DE AGRUPACION DE BISECTING KMEANS*/
val bkm = new BisectingKMeans().setK(10).setSeed(1L)

val bkm_model = bkm.fit(df)

/*6.1 RESULTADOS DE BISECTING KMEANS*/
val WSSE_bkm = bkm_model.computeCost(df)
println(s"Within set sum of Squared Errors = $WSSE_bkm")

//MUESTRAN RESULTADOS
println("Cluster Centers: ")
bkm_model.clusterCenters.foreach(println)
