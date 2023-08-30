import pprint
import model_names
import data
from functools import cmp_to_key
import data_req
import json
from operator import itemgetter

data_req_obj = data_req.DataReq()

max_price = 999
min_price = 0
budget_str = str(min_price) + "-" + str(max_price)
year = "0-"
min_power = 200
req_makes = 'skoda, volkswagen, fiat, honda, hyundai, toyota, tata, kia, mg, jeep, mahindra, ford'
search_terms = ['3.0 tdi', 'amg', 'v6', 'v 6', 'v8', 'v 8', 'v10', 'v 10', 'v12', 'v 12', 'w12', 'w 12']
data_req_obj.dump_values = True
file_path = 'car_info_all_cities.json'

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
popular_cities = data_req_obj.get_popular_cities()
make_list = data_req_obj.get_make_list('audi')
models = data_req_obj.fetch_models(data_req_obj.dump_values, body_types=None, cities=popular_cities, makes=None,
                                   budget=budget_str, year=year)
sorted_models = sorted(models, key=lambda d: int(d['priceNumeric']))
data_req_obj.max_price = max_price * 100000
data_req_obj.door = None
data_req_obj.power_req = min_power
top_n_cars = data_req_obj.fetch_car_info(sorted_models, search_terms=None, finance_req=False)
response = [data_req_obj.car_info(x, data_req_obj.dump_values) for x in top_n_cars]

response_sorted = sorted(response, key=cmp_to_key(compare))
# key=lambda x: ((int(x['priceNumeric']) * 1000000) - int(x['priceNumeric'])), reverse=True)
response_json = json.dumps(response_sorted, indent=2)
if data_req_obj.dump_values:
    print("Dumping values to {}".format(file_path))
    print(response_json, file=open(file_path, 'w'))
    print("Done")
else:
    print("\n" * 20)
    print('Total {} cars'.format(len(response_sorted)))
    print(response_json)
