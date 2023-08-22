import pprint
import model_names
import data
from functools import cmp_to_key
import data_req
import json
from operator import itemgetter

data_req_obj = data_req.DataReq()

max_price = 16
min_price = 0
budget_str = str(min_price) + "-" + str(max_price)
year = "0-13"
min_power = 230


def compare(o1, o2):
    if o1['power'] > o2['power']:
        return -1
    elif o1['power'] < o2['power']:
        return 1
    else:
        return int(o1['priceNumeric']) - int(o2['priceNumeric'])


# body_types = data_req_obj.get_body_types()
popular_cities = data_req_obj.get_popular_cities('Mumbai')
make_list = data_req_obj.get_make_list('audi, bmw, mercedes-benz, jaguar, land rover, porsche, mini')
models = data_req_obj.fetch_models(body_types=None, cities=popular_cities, makes=make_list,
                                   budget=budget_str, year=year)
sorted_models = sorted(models, key=lambda d: int(d['priceNumeric']))
data_req_obj.max_price = max_price * 100000
data_req_obj.door = None
data_req_obj.power_req = min_power
top_n_cars = data_req_obj.fetch_car_info(sorted_models, finance_req=True)
response = [data_req_obj.car_info(x) for x in top_n_cars]

response_sorted = sorted(response, key=cmp_to_key(compare))
# key=lambda x: ((int(x['priceNumeric']) * 1000000) - int(x['priceNumeric'])), reverse=True)
response_json = json.dumps(response_sorted, indent=2)
print("\n" * 20)
print('Total {} cars'.format(len(response_sorted)))
print(response_json)
