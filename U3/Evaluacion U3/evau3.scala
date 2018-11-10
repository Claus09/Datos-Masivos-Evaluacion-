import org.apache.spark.sql.SparkSession

import org.apache.log4j._
Logger.getLogger("org").setLevel(Level.ERROR)

val spark = SparkSession.builder().getOrCreate()

import org.apache.spark.ml.clustering.KMeans

val dataset  = spark.read.option("header","true").option("inferSchema", "true").format("csv").load("Wholesale_customers_data.csv")
//LIMPIEZA DE LOS DATOS DE WHOLESALE CUSTOMERS
import org.apache.spark.ml.feature.{VectorAssembler, StringIndexer, VectorIndexer, OneHotEncoder}
import org.apache.spark.ml.linalg.Vectors

val features_data = (dataset.select($"Region",$"Fresh", $"Milk",
                    $"Grocery", $"Frozen", $"Detergents_Paper", $"Delicassen"))

val assembler = (new VectorAssembler().setInputCols(Array("Fresh","Milk", "Grocery","Frozen","Detergents_Paper","Delicassen")).setOutputCol("features"))

//trains the k-means model
val kmeans = new KMeans().setK(3).setSeed(1L)
val df = assembler.transform(features_data).select($"features")
val model = kmeans.fit(df)

//--------------------------------------------------------------------------
// Evaluate clustering by calculate Within Set Sum of Squared Errors.
val WSSE = model.computeCost(df)
println(s"Within set sum of Squared Errors = $WSSE")

// Show results
println("Cluster Centers: ")
model.clusterCenters.foreach(println)
