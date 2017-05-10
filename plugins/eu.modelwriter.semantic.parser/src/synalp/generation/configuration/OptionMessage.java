package synalp.generation.configuration;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * OptionMessage indicate a category of a setting
 * @author cmoro
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
public @interface OptionMessage
{
	/** 
	 * Name of the package used 
	 */
	public String value();
}
