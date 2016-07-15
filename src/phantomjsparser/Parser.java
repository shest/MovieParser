package phantomjsparser;
import java.util.List;

public interface Parser<T extends Event>{
		 
	List<T> parse();
}
