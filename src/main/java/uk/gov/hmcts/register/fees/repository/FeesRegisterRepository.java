package uk.gov.hmcts.register.fees.repository;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.util.List;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Repository;

import com.google.gson.Gson;

import uk.gov.hmcts.register.fees.loader.Category;
import uk.gov.hmcts.register.fees.loader.Fee;
import uk.gov.hmcts.register.fees.loader.FeesRegister;

@Repository
public class FeesRegisterRepository {

	private static final Logger LOG = LoggerFactory.getLogger(FeesRegisterRepository.class);
	
	@Autowired
	private ResourceLoader resourceLoader;
	
	private  FeesRegister feesRegister = null;

	@PostConstruct
	public void init() {
		try {

			Resource resource = resourceLoader.getResource("classpath:FeesRegister.json");
			InputStream fileAsStream = resource.getInputStream();

			BufferedReader reader = new BufferedReader(new InputStreamReader(fileAsStream, "UTF-8"));

			Gson gson = new Gson();
			feesRegister = gson.fromJson(reader, FeesRegister.class);
 
			// Todo
			System.out.println("****" + feesRegister.toString());

		} catch (IOException | NullPointerException e) {
			
			LOG.error("Loading of FeesRegister.json file failed");

		}

	}

	public FeesRegister getFeesRegister() {
		if(feesRegister ==null)
			throw new FeesRegisterNotLoadedException("Loading of FeesRegister.json file failed");
		return feesRegister;
	}

	public List<Category> getAllCategories() {
		return getFeesRegister().getClaimCategories();
	}

	public Fee getFeeDetails(String eventId) {

		return getFeesRegister().getFeeDetails(eventId);
	}

	public Fee getFeeDetailsForClaimAmountAndCategory(BigDecimal amount, String claimCategoryId) {
				
		return getFeesRegister().getFeeDetailsForClaimAmountAndCategory(amount, claimCategoryId);
		
		

	}

}
