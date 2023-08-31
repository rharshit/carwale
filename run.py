import json
import time
from functools import cmp_to_key

import data_req

start = time.time()
data_req_obj = data_req.DataReq()

max_price = 999
min_price = 0
budget_str = str(min_price) + "-" + str(max_price)
year = "0-"
min_power = 200
req_makes = ['honda, hyundai, toyota, tata, kia, mg, mahindra, ford', 'skoda, volkswagen, fiat, jeep',
             'mercedes-benz, bmw, audi', 'porsche, land rover, maserati']
search_terms = ['3.0 tdi', 'amg', 'v6', 'v 6', 'v8', 'v 8', 'v10', 'v 10', 'v12', 'v 12', 'w12', 'w 12']
data_req_obj.dump_values = True
display_values = False
data_req_obj.load_offline_values = True
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


def print_values(response_obj):
    response_sorted = sorted(response_obj, key=cmp_to_key(compare))
    response_json = json.dumps(response_sorted, indent=2)
    print("\n" * 20)
    print('Total {} cars'.format(len(response_sorted)))
    print(response_json)


def dump_values(response_obj):
    response_sorted = sorted(response_obj, key=cmp_to_key(compare))
    response_json = json.dumps(response_sorted, indent=2)
    print("Dumping {} values to {}".format(len(response_obj), file_path))
    print(response_json, file=open(file_path, 'w'))
    print("Done")


# body_types = data_req_obj.get_body_types()
popular_cities = 'Mumbai, Bangalore, Delhi, Pune, Navi Mumbai, Hyderabad, Ahmedabad, Chennai, Kolkata, Chandigarh' \
    .split(", ")
popular_cities = data_req_obj.get_popular_cities()
make_list = data_req_obj.get_make_list(", ".join(req_makes[1:]))
# make_list = data_req_obj.get_make_list('bmw')
models = data_req_obj.fetch_models(file_path, body_types=None, cities=popular_cities, makes=None,
                                   budget=budget_str, year=year)
sorted_models = sorted(models, key=lambda d: int(d['priceNumeric']))
data_req_obj.max_price = max_price * 100000
data_req_obj.door = None
data_req_obj.power_req = min_power
top_n_cars = data_req_obj.fetch_car_info(sorted_models, search_terms=None, finance_req=True)

if data_req_obj.dump_values:
    response = [data_req_obj.car_info(x, True) for x in top_n_cars]
    dump_values(response)

if display_values:
    response = [data_req_obj.car_info(x, False) for x in top_n_cars]
    print_values(response)

end = time.time()
print("Total time: {}".format(end - start))
