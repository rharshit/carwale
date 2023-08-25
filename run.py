import pprint
import model_names
import data
from functools import cmp_to_key
import data_req
import json
from operator import itemgetter

data_req_obj = data_req.DataReq()

max_price = 1000
min_price = 0
budget_str = str(min_price) + "-" + str(max_price)
year = "0-"
min_power = 200
req_makes = 'skoda, volkswagen, fiat, honda, hyundai, toyota, tata, kia, mg, jeep, mahindra, ford'
search_terms = ['3.0 tdi', 'amg', 'v6', 'v 6', 'v8', 'v 8', 'v10', 'v 10', 'v12', 'v 12', 'w12', 'w 12']

def compare(o1, o2):
    if int(o1['power']) + int(o1['torque']) > int(o2['power']) + int(o2['torque']):
        return -1
    elif int(o1['power']) + int(o1['torque']) < int(o2['power']) + int(o2['torque']):
        return 1
    else:
        return int(o1['priceNumeric']) - int(o2['priceNumeric'])
    # return int(o1['priceNumeric']) - int(o2['priceNumeric'])
    # return int(o1['makeYear']) - int(o2['makeYear'])


# body_types = data_req_obj.get_body_types()
popular_cities = data_req_obj.get_popular_cities('Mumbai')
make_list = data_req_obj.get_make_list('')
models = data_req_obj.fetch_models(body_types=None, cities=popular_cities, makes=None,
                                   budget=budget_str, year=year)
sorted_models = sorted(models, key=lambda d: int(d['priceNumeric']))
data_req_obj.max_price = max_price * 100000
data_req_obj.door = None
data_req_obj.power_req = min_power
top_n_cars = data_req_obj.fetch_car_info(sorted_models, search_terms=None, finance_req=False)
response = [data_req_obj.car_info(x) for x in top_n_cars]

response_sorted = sorted(response, key=cmp_to_key(compare))
# key=lambda x: ((int(x['priceNumeric']) * 1000000) - int(x['priceNumeric'])), reverse=True)
response_json = json.dumps(response_sorted, indent=2)
print("\n" * 20)
print('Total {} cars'.format(len(response_sorted)))
print(response_json)
