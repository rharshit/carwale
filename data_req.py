import json
import time
import traceback
from concurrent.futures import ThreadPoolExecutor

import requests


class DataReq:
    cookie = None
    max_price = None
    door = None
    power_req = None
    fetched_count = 0
    total_to_fetch = 0
    temp_log_fetch_curr = 0
    dump_values = False
    load_offline_values = False
    offline_vales = []
    def get_cookie(self):
        if self.cookie is None:
            url = 'https://www.carwale.com' + "/used"
            response = requests.get(url, headers=self.get_headers(False))
            self.cookie = ""
            for keymap in response.cookies.items():
                self.cookie += " " + str(keymap[0]) + "=" + str(keymap[1]) + ";"
            self.cookie = self.cookie.strip()
        return self.cookie
        # return "CWC=gH4j6WtIaxiWxNgrlGaChau4z; CurrentLanguage=en; _abtest=91; languageSelected=en; BHC=gH4j6WtIaxiWxNgrlGaChau4z; vernacularPopupClose=1; _carSearchType=0; _gcl_au=1.1.565754526.1691499249; cebs=1; _CustZoneIdMaster=; UsedCarsCoachmark1=details|; _cwutmz=utmcsr%3Dgoogle%7Cutmgclid%3D%7Cutmccn%3D%28organic%29%7Cutmcmd%3Dorganic%7Cutmtrm%3D%7Cutmcnt%3D; _CustZoneMaster=Select%20Zone; _CustLatitude=-100; _CustLongitude=-200; _CustCityUserAction=1; _tac=false~self|not-available; _ta=in~2~883b0730c9aff671d3164a84785f6825; _dealerCityModel=30335~1~1241~0~65%2137259~1~2509~0~65%2124867~1~1845~0~65%2137459~1~1457~0~65%21; versionstate={%221241%22:%227789%22%2C%221457%22:%2213361%22%2C%221845%22:%2210133%22}; _userModelHistory=2509~1845~1457~1241; _pageviews_modelid=-1; _gid=GA1.2.1108623631.1692522018; _ce.clock_event=1; _ce.clock_data=13%2C103.74.199.38%2C1%2C3b8d399b56fb9df5592b051fde36c903; _cwutmzsrc=D%7CG%7CD%7CD%7CD%7CD%7CD%7CD%7CD%7CD%7CD%7CD%7CD%7CD%7CD; _cwutmzmed=NN%7CO%7CNN%7CNN%7CNN%7CNN%7CNN%7CNN%7CNN%7CNN%7CNN%7CNN%7CNN%7CNN%7CNN; AMP_TOKEN=%24NOT_FOUND; _CustAreaId=-1; _CustAreaName=Select Area; _ce.s=v~ce26c78df509cf951186a2846ce7fe7f4f84f759~lcw~1692570802499~vpv~0~v11.rlc~1692572684446~lcw~1692572684446; _CustCityIdMaster=1; _CustCityMaster=Mumbai; _ga=GA1.2.710250028.1691499242; cebsp_=22; _uetsid=09815e003f9911eeaaf697ef7cd36002; _uetvid=b10a1ad035ea11eeb853a5d7a4c4a222; _gat_UA-337359-1=1; _gat_pageview=1; _cwv=gH4j6WtIaxiWxNgrlGaChau4z.wOhGtYXppD.1692572653.1692573471.1692573475.8; bhs_cw=gH4j6WtIaxiWxNgrlGaChau4z.jX17TXlXbi.1692572654.1692573472.1692573476.8; _ga_Z81QVQY510=GS1.1.1692572654.8.1.1692573480.2.0.0"

    def get_headers(self, cookie=True):
        headers = {
            'accept': 'text/html,application/xhtn/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.7',
            'accept-language': 'en-US,en;q=0.9',
            'authority': 'www.carwale.com',
            'cache-control': 'no-cache',
            'cookie': 'BHC=dSr4oIJeYcjy5D3YgRjuqpifq; CWC=FmhU7PclUyaHkE4dO2imcNUGl; '
                      '_cwutmz=utmcsr%3D%28direct%29%7Cutmgclid%3D%7Cutmccn%3D%28direct%29%7Cutmcmd%3D%28none%29%7Cutmtrm%3D%7Cutmcnt%3D; '
                      'CurrentLanguage=en; _pageviews_modelid=-1; _abtest=66; '
                      '_gcl_au=1.1.107382506.1692610837; _CustCityIdMaster=-1; '
                      '_CustCityMaster=Select City; _CustAreaId=-1; _CustAreaName=Select '
                      'Area; _CustZoneIdMaster=; _CustZoneMaster=Select Zone; '
                      'UsedCarsCoachmark1=details|; AMP_TOKEN=%24NOT_FOUND; '
                      '_gid=GA1.2.83573571.1692610838; _tac=false~self|not-available; '
                      '_ta=in~2~883b0730c9aff671d3164a84785f6825; _tas=wtkrq4z58p; '
                      '_fbp=fb.1.1692610844509.1959464458; deferredDealerList=%5B%5D; '
                      '_gat_pageview=1; _ga=GA1.2.1437468085.1692610838; '
                      '_gat_UA-337359-1=1; '
                      'bhs_cw=dSr4oIJeYcjy5D3YgRjuqpifq.oKMudjKx9G.1692610836.1692611766.1692611827.1; '
                      '_ga_Z81QVQY510=GS1.1.1692610837.1.1.1692611827.59.0.0; '
                      '_cwv=FmhU7PclUyaHkE4dO2imcNUGl.oKMudjKx9G.1692610836.1692611826.1692611829.1; '
                      '_cwutmzsrc=D%7CD%7CD%7CD%7CD; _cwutmzmed=NN%7CNN%7CNN%7CNN%7CNN',
            'pragma': 'no-cache',
            'sec-ch-ua': '"Not/A)Brand";v="99", "Google Chrome";v="115", '
                         '"Chromium";v="115"',
            'sec-ch-ua-mobile': '?0',
            'sec-ch-ua-platform': '"macOS"',
            'sec-fetch-dest': 'document',
            'sec-fetch-mode': 'navigate',
            'sec-fetch-site': 'none',
            'sec-fetch-user': '?1',
            'upgrade-insecure-requests': '1',
            'user-agent': 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) '
                          'AppleWebKit/537.36 (KHTML, like Gecko) Chrome/115.0.0.0 '
                          'Safari/537.36'}
        if cookie:
            headers['cookie'] = self.get_cookie()
        return headers

    def generate_data(self, next_url):
        data_str = next_url.replace('/api/stocks/?', '')
        data = {}
        for keymap in data_str.split('&'):
            data[keymap.split("=")[0]] = str(keymap.split("=")[1]).replace("+", " ")
        return data

    def fetch_models(self, file_path, body_types=None, cities=None, makes=None, budget=None, year=None):
        start = time.time()
        print("body_types: {}".format(body_types))
        print("cities: {}".format(None if cities is None else ", ".join([city['cityName'] for city in cities])))
        print("makes: {}".format(None if makes is None else ", ".join([make['makeName'] for make in makes])))
        print("budget: {}".format(budget))
        print("age: {}".format(year))
        if cities is None:
            cities = [{'cityId': '1', 'cityName': 'Mumbai'}]
        fetched = []
        all_city_fetch = {}
        result_raw = []
        with ThreadPoolExecutor(max_workers=10) as exe:
            args = ((city, body_types, makes, budget, year) for city in cities)
            result = exe.map(self.fetch_city_models, args)
            exe.shutdown()
            result_raw = [r for r in result]
        for i in range(len(cities)):
            all_city_fetch[cities[i]['cityName']] = result_raw[i]
        print()
        for city_data in all_city_fetch.keys():
            [fetched.append(x) for x in all_city_fetch[city_data]]
        if self.load_offline_values:
            try:
                print("Loading offline values")
                data_str = open(file_path, 'r').read()
                if data_str.strip() != "":
                    self.offline_vales = json.loads(open(file_path, 'r').read())
                else:
                    self.offline_vales = []
                print("Loaded offline values")
            except:
                traceback.print_exc()
                print("Loading offline values failed")
        print()
        end = time.time()
        print("Fetching stock took {}".format(end-start))
        print()
        return list({v['profileId']: v for v in fetched if self.dump_values or len(v['stockImages'])}.values())

    def get_body_types(self):
        return ['1', '2', '5']

    def fetch_car_specs(self, model):
        start = time.time()
        rtn = {"include": self.dump_values}
        url = None
        if self.load_offline_values:
            rtn = self.get_offline_model(model)
        if rtn is None:
            try:
                url = 'https://www.carwale.com' + model['url']
                response = requests.get(url, headers=self.get_headers(False))
                if response.status_code != 200:
                    print("Fetching URL returned {}: {}".format(response.status_code, url))
                    if self.dump_values:
                        model['power'] = 0
                        model['torque'] = 0
                        model['success'] = False
                        model['include'] = self.dump_values
                        return model
                    else:
                        return None
                try:
                    from BeautifulSoup import BeautifulSoup
                except ImportError:
                    from bs4 import BeautifulSoup
                parsed_html = BeautifulSoup(response.text, 'html.parser')
                property_wrapper_tags = parsed_html.find_all('ul', class_='o-cpnuEd o-XylGE o-eNbQSA o-bIMsfE o-djSZRV o-GFmfi')
                for property_wrapper_tag in property_wrapper_tags:
                    prop_key_map = property_wrapper_tag.find_all('li')
                    if len(prop_key_map) >= 2:
                        model[str(prop_key_map[0].get_text()).lower()] = prop_key_map[1].get_text()
                if 'max power (bhp@rpm)' in model.keys():
                    try:
                        power_str = model['max power (bhp@rpm)']
                        power_split = power_str.split("@")
                        power = int(str(power_split[0]).strip().split(" ")[0])
                        model['power'] = power
                    except:
                        x = 1
                if 'max torque (nm@rpm)' in model.keys():
                    try:
                        torque_str = model['max torque (nm@rpm)']
                        torque_split = torque_str.split("@")
                        torque = int(str(torque_split[0]).strip().split(" ")[0])
                        model['torque'] = torque
                    except:
                        x = 1
                if 'power' not in model.keys():
                    model['power'] = 0
                if 'torque' not in model.keys():
                    model['torque'] = 0
                rtn = model
                rtn['success'] = True
            except:
                # traceback.print_exc()
                rtn = model
                rtn['power'] = 0
                rtn['torque'] = 0
                rtn['success'] = False
                rtn['include'] = self.dump_values
                print("Failed to fetch url: {}".format(url))
        # return model
        if self.dump_values or (self.door is None or (
                self.door is not None and 'self.doors' in rtn.keys() and str(self.door) not in rtn['self.doors'])) \
                and (self.power_req is None or
                     (self.power_req is not None and 'power' in rtn.keys() and
                      (int(rtn['power']) == 0 or int(rtn['power']) >= int(self.power_req)))):
            rtn['include'] = True
        else:
            rtn['include'] = False
        self.fetched_count += 1
        self.temp_log_fetch_curr += 1
        if self.temp_log_fetch_curr > 25:
            print("Fetched {}%".format(self.fetched_count * 100 // self.total_to_fetch, self.total_to_fetch))
            self.temp_log_fetch_curr = 0
        end = time.time()
        rtn['timeToFetch'] = end-start
        return rtn

    def fetch_car_info(self, sorted_models, search_terms=None, finance_req=False):
        start = time.time()
        print("Fetching car infos")
        search_filter = [x for x in sorted_models if self.dump_values or (search_terms is None
                                                                          or sum(
                    [1 if str(y).strip().upper() in str(x['carName']).upper() else 0 for y in search_terms]) > 0)]
        filtered_models = [x for x in search_filter if self.dump_values or
                           ((self.max_price is None or int(x['priceNumeric']) <= self.max_price)
                            and (not finance_req or (finance_req and x['isEligibleForFinance'])))]
        self.total_to_fetch = len(filtered_models)
        print("Eligible cars: {} of {}".format(len(filtered_models), len(sorted_models)))
        with ThreadPoolExecutor(max_workers=350) as exe:
            result = exe.map(self.fetch_car_specs, filtered_models)
            exe.shutdown()
            result_raw = [r for r in result]
            top_n = [r for r in result_raw if 'include' not in r.keys() or r['include']]
            end = time.time()
            print()
            print("Fetching all specs took {}".format(end - start))
            if len([r for r in result_raw]) > 0:
                average_time_to_fetch = sum([x['timeToFetch'] for x in result_raw]) / len(result_raw)
                print("Fetching each spec took {}".format(average_time_to_fetch))
        if self.dump_values:
            start = time.time()
            print("Merging offline data")
            [top_n.append(x) for x in self.offline_vales if x['profileId'] not in [y['profileId'] for y in top_n]]
            end = time.time()
            print("Merged in {}".format(end - start))
            print()
        return top_n

    def get_popular_cities(self, city_name=None):
        cities = []
        try:
            response = requests.get("https://www.carwale.com/api/popular-cities/", headers=self.get_headers())
            data = json.loads(response.text)
            for city in data:
                if city_name is None \
                        or str(city['cityName']).upper().strip() in [str(x).upper().strip() for x in
                                                                     city_name.split(",")]:
                    cities.append(city)
        except Exception:
            traceback.print_exc()
        return cities

    def get_make_list(self, makes_str=None):
        makes = []
        try:
            params = {"type": "used", "application": "1", "year": "2023", "isActive": "false"}
            response = requests.get("https://www.carwale.com/api/v2/models/", headers=self.get_headers(), params=params)
            data = json.loads(response.text)
            for make in data['makeList']:
                if makes_str is None \
                        or str(make['makeName']).strip().upper() in [str(x).strip().upper() for x in
                                                                     makes_str.split(",")]:
                    makes.append(make)
        except:
            traceback.print_exc()
        return makes

    def car_info(self, model, dump_values):
        keys = ["carName", "url", "valuationUrl", "makeYear", "price", "priceNumeric", "km", "kmNumeric", "cityName",
                "areaName", "isEligibleForFinance", "isPremium", "max power (bhp@rpm)", "max torque (nm@rpm)",
                "drivetrain", "doors", "engine", "engine type", "fuel type", "power", "torque", "isEligibleForFinance"]
        car = {}
        for key in model.keys():
            if key.strip().upper() in [x.strip().upper() for x in keys] or dump_values:
                val = model[key]
                if 'URL' in key.strip().upper():
                    val = str(val).strip().replace('https://www.carwale.com', '')
                    if not str(val).strip().startswith('https://www.carwale.com'):
                        val = 'https://www.carwale.com' + str(val).strip()
                car[key] = val
        return car

    def get_offline_model(self, model):
        try:
            for offline_model in self.offline_vales:
                if 'success' in offline_model.keys() and offline_model['success'] \
                        and model['profileId'] == offline_model['profileId'] \
                        and model['priceNumeric'] == offline_model['priceNumeric'] \
                        and model['kmNumeric'] == offline_model['kmNumeric']:
                    return offline_model
        except:
            x = 1
        return None

    def fetch_city_models(self, args):
        fetch_num = []
        city_fetch = []
        city = args[0]
        body_types = args[1]
        makes = args[2]
        budget = args[3]
        year = args[4]
        total_cars = 0
        try:
            init_url = 'https://www.carwale.com/api/stocks/filters/'
            data = {"pn": "1", "kms": "0-", "city": str(city['cityId']), "so": "-1", "sc": "-1"}
            if body_types is not None:
                data['bodytype'] = " ".join(body_types)
            if makes is not None:
                data['car'] = " ".join([str(x['makeId']).strip() for x in makes])
            if budget is not None:
                data['budget'] = budget
            else:
                data['budget'] = "0-"
            if year is not None:
                data['year'] = year
            else:
                data['year'] = "0-"
            try:
                response = requests.post(init_url, headers=self.get_headers(), json=data)
                if response.status_code == 200:
                    [city_fetch.append(x) for x in json.loads(response.text)['stocks']]
                    city_fetch = list(
                        {v['profileId']: v for v in city_fetch if self.dump_values or len(v['stockImages'])}.values())
                    next_url = json.loads(response.text)['nextPageUrl']
                    total_cars = json.loads(response.text)['totalCount']
                    fetch_num.append(len(city_fetch))
                else:
                    raise Exception
            except Exception:
                next_url = None
            while next_url is not None and (len(fetch_num) < 3 or (
                    len(fetch_num) >= 3 and fetch_num[-1] - fetch_num[-2] != 0 and fetch_num[-2] - fetch_num[-3] != 0)):
                try:
                    data = self.generate_data(next_url)
                    response = requests.post(init_url, headers=self.get_headers(), json=data)
                    if response.status_code == 200:
                        [city_fetch.append(x) for x in json.loads(response.text)['stocks']]
                        city_fetch = list(
                            {v['profileId']: v for v in city_fetch if
                             self.dump_values or len(v['stockImages'])}.values())
                        fetch_num.append(len(city_fetch))
                        next_url = json.loads(response.text)['nextPageUrl']
                    else:
                        raise Exception
                except Exception:
                    next_url = None
            print("Fetched stock: {} out of {} in {}".format(str(len(city_fetch)), total_cars, city['cityName']))
        except:
            traceback.print_exc()
        return city_fetch
