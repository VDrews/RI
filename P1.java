
public class P1 {
  public static void main(String[] args) {
    System.out.println(args[0]);
    if (args[0].equals("-d")) {

      // Realizar de fotma automatica una tabla que contenga el nombre del fichero,
      // tipo, codificacion e idioma.
    } else if (args[0].equals("-l")) {
      System.out.println("Parametro L");
      // Recuperar todos los enlaces
    } else if (args[0].equals("-t")) {

    } else {
      System.out.println("Número de argumentos incorrectos. Argumentos: -d ; -l ; -t");
    }
  }

}
