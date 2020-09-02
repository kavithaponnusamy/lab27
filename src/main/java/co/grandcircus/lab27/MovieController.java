package co.grandcircus.lab27;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
public class MovieController {
	
	@Autowired
	private MovieDao dao;
	
	@GetMapping("/movies")
	public List<Movie> listMovies(@RequestParam(required=false) String title,@RequestParam(required=false) String category){
		if((title==null || title.isEmpty())&&(category==null || category.isEmpty())) {
			return dao.findAll();
		}else {
			if((title==null || title.isEmpty())) {
				return dao.findByCategoryContainsIgnoreCase(category);
			}
			else  {
				return dao.findByTitleContainsIgnoreCase(title);
			}

		}
	}
	@GetMapping("/movies/{id}")
	public Movie findSpecificMovie(@PathVariable(required=false) Long id) {
		return dao.findById(id).orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND, "No such movie"));
		
	}
	@GetMapping("/random-movie")
	public Movie displayARandomMovie(@RequestParam(required=false) String category) {
		
		Long count=  dao.count();
		
		Random rand = new Random();
		int res=rand.nextInt(count.intValue())+1; 
		// nextInt returns a value between 0 (inclusive) and count (exclusive). 
		// so, adding 1 to get a value between 1 to count.
		
		if(category==null || category.isEmpty()) {
			return  dao.findById(Long.valueOf(res)).get();
		}
		else
		{
			List<Movie> movie=dao.findByCategoryContainsIgnoreCase(category);
			res = rand.nextInt(movie.size());
			return movie.get(res);
		}
	}
	@GetMapping("/random-movies")
	public List<Movie> displayARandomMovies(@RequestParam(required=false) Integer quantity) {
		List<Movie> list=new  ArrayList<>();
		Long count = dao.count();
		Random rand = new Random();
		int res;
		
		for(int i=0;i<quantity;i++) {
			 res= rand.nextInt(count.intValue())+1;
			 // nextInt returns a value between 0 (inclusive) and count (exclusive). 
			 // so, adding 1 to get a value between 1 to count.
			 list.add(dao.findById(Long.valueOf(res)).get());
			}
		return list;
	}
	@GetMapping("/categories")
	public List<String> displayCategories() {
		return dao.getDistinctCategory();
	}
		

}