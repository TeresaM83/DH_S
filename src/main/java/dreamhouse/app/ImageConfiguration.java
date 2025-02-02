package dreamhouse.app;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class ImageConfiguration implements WebMvcConfigurer {

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		WebMvcConfigurer.super.addResourceHandlers(registry);
		registry.addResourceHandler("/imagesMantenimiento/**").addResourceLocations("file:/D:/DH/imagesMantenimiento/");
		registry.addResourceHandler("/imagesUsuarios/**").addResourceLocations("file:/D:/DH/imagesUsuarios/");
	}
}
