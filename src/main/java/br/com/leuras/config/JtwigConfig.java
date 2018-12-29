package br.com.leuras.config;

import java.nio.charset.Charset;

import org.jtwig.environment.EnvironmentConfiguration;
import org.jtwig.environment.EnvironmentConfigurationBuilder;
import org.jtwig.spring.JtwigViewResolver;
import org.jtwig.spring.asset.SpringAssetExtension;
import org.jtwig.spring.boot.config.JtwigViewResolverConfigurer;
import org.jtwig.web.servlet.JtwigRenderer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@EnableWebMvc
@Configuration
public class JtwigConfig extends WebMvcConfigurerAdapter implements JtwigViewResolverConfigurer {
	
	@Value("${spring.jtwig.assets-handler}")
	private String resourceHandler;
	
	@Value("${spring.jtwig.assets-location}")
	private String resourceLocation;
	
	@Value("${spring.jtwig.charset}")
	private String charset;
	
	@Override
	public void configure(final JtwigViewResolver resolver) {
		
		final EnvironmentConfiguration config = EnvironmentConfigurationBuilder.configuration()
				.resources()
				.withDefaultInputCharset(Charset.forName(this.charset))
				.and()
				.render()
				.withOutputCharset(Charset.forName(this.charset))
				.and()
				.parser()
				.withoutTemplateCache()
				.and()
				.extensions().add(new SpringAssetExtension())
				.and()
				.build();

		resolver.setRenderer(new JtwigRenderer(config));
	}
		
	@Override
	public void addResourceHandlers(final ResourceHandlerRegistry registry) {
		registry
			.addResourceHandler(this.resourceHandler)
			.addResourceLocations(this.resourceLocation);
	}
}
