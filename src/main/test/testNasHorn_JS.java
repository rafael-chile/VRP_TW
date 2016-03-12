import com.vrptw.entities.Client;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.time.LocalDateTime;
import java.util.Date;

// Exemple obtaiend from : http://winterbe.com/posts/2014/04/05/java8-nashorn-tutorial/
public class testNasHorn_JS {

    public static void main(String... str) throws FileNotFoundException, ScriptException, NoSuchMethodException {
        ScriptEngine engine = new ScriptEngineManager().getEngineByName("nashorn");
        engine.eval(new FileReader("src/main/test/mapquest/testScript.js"));
        engine.eval(new FileReader("src/main/test/mapquest/mapquest.js"));

        Invocable invocable = (Invocable) engine;

        Object result = invocable.invokeFunction("fun1", "Peter Parker");
        System.out.println(result);
        System.out.println(result.getClass());

        invocable.invokeFunction("fun2", new Date());

        invocable.invokeFunction("fun2", LocalDateTime.now());

        invocable.invokeFunction("fun2", new Client());
    }
}
