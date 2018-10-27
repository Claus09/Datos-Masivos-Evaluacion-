
import org.apache.spark.ml.feature.LabeledPoint
import org.apache.spark.rdd.RDD
import org.apache.spark.mllib.linalg.Vectors

import org.apache.spark.sql.SparkSession
val spar = SparkSession.builder().getOrCreate()

//1.- LIMPIEZA DE DATOS
/*
Primero se carga el archivo CSV en una variable val de tipo RDD[String], posteriormente en una variable val de
tipo RDD[LabeledPoint] mapearemos lo que contiene raw con los parámetros que nostros queremos:
Nuestro LabeledPoint tendrá como primer parámetro las clases (setosa, versicolor, virginica) que son dados por
resultados de los case como 1.0,2.0,3.0 respectivamente y un caso default si es que no cumple con alguno de ellos.
Utilizamos parts(0) para indicarle dentro de Vectors.dense (que es nuestro segundo parámetro de LabeledPoint) que
queremos los elementos de la columna que se desea, en este caso necesitamos las primeras 4 colummnas y esto lo
transformamos a vector tipo ml usando .asML
Por último convertimos en una variable val nuestro archivi limpio como un Dataframe con las columna "label"
y "features" que son las que ocupamos para el algoritmo de MLP.
*/
val raw: RDD[String] = sc.textFile("Iris.csv")

val cldata: RDD[LabeledPoint] = raw.map{line =>
  val parts = line.split(',')
  LabeledPoint(parts(4) match {
    case "Iris-setosa" => 1.0
    case "Iris-versicolor" => 2.0
    case "Iris-virginica" => 3.0
    case _ => 0.0
  }, Vectors.dense(parts(0).toDouble,parts(1).toDouble,parts(2).toDouble,parts(3).toDouble).asML)
  }.cache()


val df = cldata.toDF("label","features")
//---------------------------------------------------

//ALGORITMO ML PERCPETRÓN CAPA MULTIPLE (MLP)
//a.
import org.apache.spark.ml.classification.MultilayerPerceptronClassifier
import org.apache.spark.ml.evaluation.MulticlassClassificationEvaluator

//Se seleccionó un 60% de entrenamiento y un 40% de prueba con una semilla 1234L que hace referencia a los pesos (long)
val splits = df.randomSplit(Array(0.6, 0.4), seed = 1234L)
//Guardamos nuestras sepraciones de pentrenamiento y de prueba
val train = splits(0)
val test = splits(1)
//---------------------------------------------------

//b.-
//Se especifican los nodos de nuestras capas
// La capa de entrada será 4 (4 características), dos nodos escondidos de 4 cada uno y 4 de salida (clases)
val layers = Array[Int](4, 4, 4, 4)
//---------------------------------------------------

//c.-
// Se crea el entrenador y se asignan los parametros
//.setLayers(layers) se usa para asignar las capas que se crearon previamente (3 de entrada, dos intermedias de 4 y 150 de salida)
//.setSeed es la asignación aleatoria de la semilla que indica los pesos iniciales en caso de no haber sido asignados.
//.setMaxIter es el numero máximo de iteraciones por la cuál se realizarán los cálculos.
//.setBlockSize es el tamaño del bloque para poner los datos de entrada en matrices que nos ayudan a agilizar los cálculos
val trainer = new MultilayerPerceptronClassifier().setLayers(layers).setBlockSize(128).setSeed(1234L).setMaxIter(100)
// Entrenamos el modelo
val model = trainer.fit(train)
// Calculamos la precisión de nuestro modelo
val result = model.transform(test)
//Seleccionamos la etiqueta junto con su predicción
val predictionAndLabels = result.select("prediction", "label")
//A nuestro evaluador se le asignará la métrica de precisión, de ésta manera podermos saber que tan preciso es nuestro modelo
val evaluator = new MulticlassClassificationEvaluator().setMetricName("accuracy")
//Se imprime el resultado de la precisión de los datos de prueba
println(s"Test set accuracy = ${evaluator.evaluate(predictionAndLabels)}")

//d.-
/* La función matemática que se implementa de manera predeterminada en los
nodos intermedios es la función sigmoide (1/1+e^-n), se utiliza como función de
activación que nos ayuda con datos binarios o múltiples mientras que en los
nodos de salida se utiliza la función softmax (e^(xi) / sum(e^(zk) )) que nos
ayuda con la clasificación múltiple así como los cálculos de predicciones para
las mismas*/

//e.-
/* La función para calcular los nuevos pesos fue (w<t+1> = w + b(error)(z)) que
nos ayuda a asignar nuevos pesos para la siguiente iteración, la formula para
calcular el error por predeterminado es un algoritmo de optimización llamado
L-BFGS usando una limitada cantidad de memoria, se usa para estimación de
parametros.*/

//f.-
//El ejercicio se encuentra en github
