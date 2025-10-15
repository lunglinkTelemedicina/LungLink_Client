package org.example;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    static void main() {
        //TIP Press <shortcut actionId="ShowIntentionActions"/> with your caret at the highlighted text
        // to see how IntelliJ IDEA suggests fixing it.
        IO.println(String.format("Hello and welcome!"));
        //Comida: la crema de garbanzos es una forma muy sencilla y sabrosa de disfrutar de una buena ración de legumbres. Para continuar, proponemos unos espaguetis a la boloñesa, un clásico de la cocina casera que no puede estar más rico.
        //Cena: el revuelto de bacalao es un plato muy nutritivo y sencillo de preparar, estupendo para disfrutar de una cena rápida.


        for (int i = 1; i <= 5; i++) {
            //TIP Press <shortcut actionId="Debug"/> to start debugging your code. We have set one <icon src="AllIcons.Debugger.Db_set_breakpoint"/> breakpoint
            // for you, but you can always add more by pressing <shortcut actionId="ToggleLineBreakpoint"/>.
            IO.println("i = " + i);
        }
    }
}
