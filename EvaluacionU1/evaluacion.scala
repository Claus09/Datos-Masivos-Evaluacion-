//1.- Revisar si un numero es par o impar
def even(num:Int):Int = {
  if(num % 2 == 0){
    return 1
  }
  else {
    return 0
  }
}

even(2)
even(7)

//2.- Revisar si la lista contiene un numero par o no
def evenlt(num:List[Int]) = {
  var bool = false
  for(i <- num){
      if(i % 2 == 0) {
        bool = true} else {
        bool = false
        }
        println(bool)
      }
    }

val mylist = List(1,1,3)
val mylist2 = List(1,3,7,2)
evenlt(mylist)
evenlt(mylist2)

//3.- Lucky 7 (Si hay 7 entonces valen por 2)
def lucky_seven(n:List[Int]):Int = {
  var re = n.sum
  for (i <- n){
    if (i == 7) {
        re = re + i
    }
  }
  return re
}

val lucky = List(1,2,7,7)
lucky_seven(lucky)

//1 + 2 + 7 + 7 = 31

//4.- Equilibrio (si una lista puede partirse en dos partes iguales o no)
def equil(re:List[Int]): Boolean = {
if(re.length % 2 == 0){
  return true} else {
    return false
  }
}

val eq1 = List(1,5,3,3)
val eq2 = List(1,3,5)
equil(eq1)
equil(eq2)

//5.- Revisar si un string es palíndromo (su reverso es igual al string original)
def rev(a:String) = {
  if(a == a.reverse){
    println("Es palíndromo")
  } else {
  println("No es palíndromo")
 }
}

val str1 = "anna"
val str2 = "juan"
rev(str1)
rev(str2)
