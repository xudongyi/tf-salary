package business.util;

import io.micrometer.core.lang.Nullable;
import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.boot.env.YamlPropertySourceLoader;
import org.springframework.core.env.PropertiesPropertySource;
import org.springframework.core.env.PropertySource;
import org.springframework.core.io.support.EncodedResource;
import org.springframework.core.io.support.PropertySourceFactory;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class YamlPropertySourceFactory implements PropertySourceFactory {
    public YamlPropertySourceFactory () {
    }

    /**
     * yaml 文档解析方法
     *
     * @param name     配置项名称
     * @param resource 配置项资源
     * @return PropertySource<?>
     * @throws IOException IOException
     * @since 1.0
     */
    @Override
    public PropertySource<?> createPropertySource (@Nullable String name, EncodedResource resource) throws IOException {

        // 返回 yaml 属性资源
        return new YamlPropertySourceLoader()
                .load (resource.getResource ().getFilename (), resource.getResource ())
                .get (0);
    }

}