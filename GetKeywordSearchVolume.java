package adwords.axis.v201607.optimization;

import com.google.api.ads.adwords.axis.factory.AdWordsServices;
import com.google.api.ads.adwords.axis.v201607.cm.Language;
import com.google.api.ads.adwords.axis.v201607.cm.NetworkSetting;
import com.google.api.ads.adwords.axis.v201607.cm.Paging;
import com.google.api.ads.adwords.axis.v201607.o.Attribute;
import com.google.api.ads.adwords.axis.v201607.o.AttributeType;
import com.google.api.ads.adwords.axis.v201607.o.IdeaType;
import com.google.api.ads.adwords.axis.v201607.o.IntegerSetAttribute;
import com.google.api.ads.adwords.axis.v201607.o.LanguageSearchParameter;
import com.google.api.ads.adwords.axis.v201607.o.LongAttribute;
import com.google.api.ads.adwords.axis.v201607.o.MonthlySearchVolume;
import com.google.api.ads.adwords.axis.v201607.o.MonthlySearchVolumeAttribute;
import com.google.api.ads.adwords.axis.v201607.o.NetworkSearchParameter;
import com.google.api.ads.adwords.axis.v201607.o.RelatedToQuerySearchParameter;
import com.google.api.ads.adwords.axis.v201607.o.RequestType;
import com.google.api.ads.adwords.axis.v201607.o.SearchParameter;
import com.google.api.ads.adwords.axis.v201607.o.StringAttribute;
import com.google.api.ads.adwords.axis.v201607.o.TargetingIdea;
import com.google.api.ads.adwords.axis.v201607.o.TargetingIdeaPage;
import com.google.api.ads.adwords.axis.v201607.o.TargetingIdeaSelector;
import com.google.api.ads.adwords.axis.v201607.o.TargetingIdeaServiceInterface;
import com.google.api.ads.adwords.lib.client.AdWordsSession;
import com.google.api.ads.common.lib.auth.OfflineCredentials;
import com.google.api.ads.common.lib.auth.OfflineCredentials.Api;
import com.google.api.ads.common.lib.utils.Maps;
import com.google.api.client.auth.oauth2.Credential;
import com.google.common.base.Joiner;
import com.google.common.primitives.Ints;

import java.util.Arrays;
import java.util.Map;

/**
* This example gets keywords related to a seed keyword.
*
* <p>Credentials and properties in {@code fromFile()} are pulled from the
* "ads.properties" file. See README for more info.
*/
public class _2GetKeywordSearchVolume {

public static void main(String[] args) throws Exception {
 // Generate a refreshable OAuth2 credential.
 Credential oAuth2Credential = new OfflineCredentials.Builder()
     .forApi(Api.ADWORDS)
     .fromFile()
     .build()
     .generateCredential();

 // Construct an AdWordsSession.
 AdWordsSession session = new AdWordsSession.Builder()
     .fromFile()
     .withOAuth2Credential(oAuth2Credential)
     .build();

 AdWordsServices adWordsServices = new AdWordsServices();

 runExample(adWordsServices, session);
}

public static void runExample(
   AdWordsServices adWordsServices, AdWordsSession session) throws Exception {
 // Get the TargetingIdeaService.
 TargetingIdeaServiceInterface targetingIdeaService =
     adWordsServices.get(session, TargetingIdeaServiceInterface.class);

 
 // Create selector.
 // firstly, prepare the TargetingIdeaSelector with parameters that specify this request is to retrieve keyword ideas:
 TargetingIdeaSelector selector = new TargetingIdeaSelector();
 selector.setRequestType(RequestType.STATS);
 selector.setIdeaType(IdeaType.KEYWORD);
 selector.setRequestedAttributeTypes(new AttributeType[] {
	 AttributeType.KEYWORD_TEXT,
     AttributeType.SEARCH_VOLUME,
     AttributeType.TARGETED_MONTHLY_SEARCHES});
 

 // Create related to query search parameter to specify a list of seed keywords from which to generate new ideas:
 RelatedToQuerySearchParameter relatedToQuerySearchParameter = new RelatedToQuerySearchParameter();
 relatedToQuerySearchParameter.setQueries(new String[] {"goldstrand party"});
 
 
 // Language setting (optional).
 // The ID can be found in the documentation:
 //   https://developers.google.com/adwords/api/docs/appendix/languagecodes
 // See the documentation for limits on the number of allowed language parameters:
 //   https://developers.google.com/adwords/api/docs/reference/latest/TargetingIdeaService.LanguageSearchParameter
 
 LanguageSearchParameter languageParameter = new LanguageSearchParameter();
 Language german = new Language();
 german.setId(1001L);
 languageParameter.setLanguages(new Language[] {german});
 
 //Create network search parameter (optional).
 NetworkSetting networkSetting = new NetworkSetting();
 networkSetting.setTargetGoogleSearch(true);
 networkSetting.setTargetSearchNetwork(false);
 networkSetting.setTargetContentNetwork(false);
 networkSetting.setTargetPartnerSearchNetwork(false);

 NetworkSearchParameter networkSearchParameter = new NetworkSearchParameter();
 networkSearchParameter.setNetworkSetting(networkSetting);
 
 //Set selector paging (required for targeting idea service).
 Paging paging = new Paging();
 paging.setStartIndex(0);
 paging.setNumberResults(100);
 selector.setPaging(paging);
 

 selector.setSearchParameters(new SearchParameter[] {relatedToQuerySearchParameter, languageParameter, networkSearchParameter});

 // Get related keywords.
 // Once you've configured the TargetingIdeaSelector, send it through the get operation to retrieve keyword ideas:
 TargetingIdeaPage page = targetingIdeaService.get(selector);

 // Display related keywords.
 if (page.getEntries() != null && page.getEntries().length > 0) {
   for (TargetingIdea targetingIdea : page.getEntries()) {
     Map<AttributeType, Attribute> data = Maps.toMap(targetingIdea.getData());
     String kwd = ((StringAttribute) data.get(AttributeType.KEYWORD_TEXT)).getValue();
     Long monthlySearches = ((LongAttribute) data.get(AttributeType.SEARCH_VOLUME)).getValue();
     MonthlySearchVolume[] categories = ((MonthlySearchVolumeAttribute) data.get(AttributeType.TARGETED_MONTHLY_SEARCHES)).getValue();
     
     
     System.out.println("Year - Month - Count");
        for(MonthlySearchVolume model : categories) {
            System.out.println(model.getYear() +" "+ model.getMonth() + " " + model.getCount());
        }
   
   // System.out.println(Arrays.asList(monthlySearches[1]));
    
   }
 } else {
   System.out.println("No related keywords were found.");
 }
}
}


