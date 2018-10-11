package autocorrect.config;

import autocorrect.controllers.AppErrorController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GeneralConfig
{
    @Autowired
    private ErrorAttributes errorAttributes;

    @Bean
    public AppErrorController appErrorController() { return new AppErrorController(errorAttributes); }
}
