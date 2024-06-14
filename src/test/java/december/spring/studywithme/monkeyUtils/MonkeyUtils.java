package december.spring.studywithme.monkeyUtils;

import com.navercorp.fixturemonkey.FixtureMonkey;
import com.navercorp.fixturemonkey.api.introspector.BuilderArbitraryIntrospector;
import com.navercorp.fixturemonkey.api.introspector.ConstructorPropertiesArbitraryIntrospector;
import com.navercorp.fixturemonkey.api.introspector.FieldReflectionArbitraryIntrospector;
import com.navercorp.fixturemonkey.jakarta.validation.plugin.JakartaValidationPlugin;

public class MonkeyUtils {

    public static FixtureMonkey monkey() {
        return FixtureMonkey.builder()
                .objectIntrospector(FieldReflectionArbitraryIntrospector.INSTANCE)
                .build();
    }

    public static FixtureMonkey validMonkey() {
        return FixtureMonkey.builder()
                .plugin(new JakartaValidationPlugin())
                .objectIntrospector(FieldReflectionArbitraryIntrospector.INSTANCE)
                .build();
    }
}