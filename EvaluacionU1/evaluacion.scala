//1.- si es par o no
def even(num:Int):Int = {
  if(num % 2 == 0){
    return 1
  }
  else {
    return 0
  }
}
//val a = 2;val b = 3; if(a % 2 == 0) println("Es par") else ("Es impar")

//2.- pares
//def even(num:List[Int]):List[Int] = {
def even(num:List[Int]):Boolean = {
  var bool = false
  for(i <- num){
    if(num(i) % 2 == 0){
      bool = true
    }
    else {
      bool = false
    }
  }
  return bool
}



//3.- lucky seven
/*val enteros = List(7,7)
val enteros_sinrepetir = enteros.toSet
enteros_sinrepetir.sum = enteros.sum*/
val enteros = List(1,2,7,7)
def lucky_seven(n:List[Int]):Int = {
  var re = n.sum
  for (i <- n){
    if (i == 7) {
        re = re + i
    }
  }
  return re
}

//4.- equilibrio
val enteros = List(1,5,3,3)
//enteros.slice(0,(enteros.lengt /2))
//if(enteros.slice(0,(enteros.length / 2)) == enteros.length / 2)
if(enteros.length % 2 == 0)
  println("True")
  else {
    println("False")
  }

//5.- palíndromo
val a = "anna"
if(a == a.reverse){
  println("Es palíndromo")
}
else {
  println("No es palíndromo")
}
