package org.tonylin.stock.dataprovider.parser;

import org.htmlparser.Node;
import org.htmlparser.NodeFilter;
import org.htmlparser.filters.AndFilter;
import org.htmlparser.filters.HasAttributeFilter;
import org.htmlparser.filters.TagNameFilter;

public class TagWithAttributesFilter implements NodeFilter {
	private static final long serialVersionUID = 3054463150318375728L;
	private NodeFilter mTheFilter = null;
	
	public TagWithAttributesFilter(String tagName, String attributeName, 
			String attributeValue){
		NodeFilter tagNF = new TagNameFilter(tagName);
		NodeFilter hasAttrNF = new HasAttributeFilter(
				attributeName, attributeValue);
		mTheFilter = new AndFilter(tagNF, hasAttrNF);
	}
	
	@Override
	public boolean accept(Node arg0) {
		if( mTheFilter == null ){
			return true;
		}
		return mTheFilter.accept(arg0);
	}

}
