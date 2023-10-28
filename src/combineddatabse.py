##this file combines two different Kaggle datasets##

from urllib.request import urlopen
##assigns url to a variable and allows python to be able to read the information on the html##
url = "https://www.kaggle.com/datasets/shrutikunapuli/ndc-code-lookup"
html_page = urlopen(url)
html_text = html_page.read().decode("utf-8")

##goes through URL and finds specific information to scrape##
for string in ["NDC_Code","Proprietary Name","Non-proprietary Name","Company Name"]:
    string_start_idx = html_text.find(string)
    text_start_idx = string_start_idx +len(string)

