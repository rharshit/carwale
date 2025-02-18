/* eslint-disable @typescript-eslint/no-unused-vars */
import { Button, Flex, Segmented, Typography } from "antd";
import { SegmentedOptions } from "antd/es/segmented";
import { valueType } from "antd/es/statistic/utils";
import { useEffect, useState } from "react";
import { get } from "../../../service/api";
import CarFilter from "./CarFilter";
import CityFilter from "./CityFilter";
import HeightFilter from "./HeightFilter";
import LengthFilter from "./LengthFilter";
import { default as PowerFilter } from "./PowerFilter";
import { SliderFilter } from "./SliderFilter";
import TorqueFilter from "./TorqueFilter";
import WheelbaseFilter from "./WheelbaseFilter";
import WidthFilter from "./WidthFilter";

const { Title, Paragraph, Text, Link } = Typography;

type FilterProps = {
    onApplyFilter: (carFilter: CarFilter) => void
}

export function FilterComponent(filterProps: FilterProps) {
    const { onApplyFilter } = filterProps

    const [selectedFilter, setSelectedFilter] = useState<string | number>();
    const [carFilterValues, setCarFilterValues] = useState<CarFilterResponse>();

    const [isCityFilterEnabled, setCityFilterEnabled] = useState<boolean>(false);
    const [selectedCities, setSelectedCities] = useState<string[]>([]);

    //

    const [isYearFilterEnabled, setYearFilterEnabled] = useState<boolean>(false);
    const [minYear, setMinYear] = useState<number>(0);
    const [maxYear, setMaxYear] = useState<number>(0);

    const [isPriceFilterEnabled, setPriceFilterEnabled] = useState<boolean>(false);
    const [minPrice, setMinPrice] = useState<number>(0);
    const [maxPrice, setMaxPrice] = useState<number>(0);

    const [isMileageFilterEnabled, setMileageFilterEnabled] = useState<boolean>(false);
    const [minMileage, setMinMileage] = useState<number>(0);
    const [maxMileage, setMaxMileage] = useState<number>(0);

    const filterOptions: SegmentedOptions<valueType> = [
        {
            label: (
                <Text disabled={selectedFilter != 'City' && !isCityFilterEnabled}>City</Text>
            ),
            value: 'City'
        },
        {
            label: (
                <Text>Car</Text>
            ),
            value: 'Car'
        },
        {
            label: (
                <Text disabled={selectedFilter != 'Year' && !isYearFilterEnabled}>Year</Text>
            ),
            value: 'Year'
        },
        {
            label: (
                <Text disabled={selectedFilter != 'Price' && !isPriceFilterEnabled}>Price</Text>
            ),
            value: 'Price'
        },
        {
            label: (
                <Text disabled={selectedFilter != 'Mileage' && !isMileageFilterEnabled}>Mileage</Text>
            ),
            value: 'Mileage'
        },
        {
            label: (
                <Text>Power</Text>
            ),
            value: 'Power'
        },
        {
            label: (
                <Text>Torque</Text>
            ),
            value: 'Torque'
        },
        {
            label: (
                <Text>Length</Text>
            ),
            value: 'Length'
        },
        {
            label: (
                <Text>Width</Text>
            ),
            value: 'Width'
        },
        {
            label: (
                <Text>Height</Text>
            ),
            value: 'Height'
        },
        {
            label: (
                <Text>Wheelbase</Text>
            ),
            value: 'Wheelbase'
        },
    ]

    useEffect(() => {
        loadFilters();
    }, []);

    function loadFilters() {
        Promise.resolve(get('/car/filters').then(res => {
            populateFilters(res as CarFilterResponse)
        }))
    }

    function populateFilters(carFilterValues: CarFilterResponse) {
        setCarFilterValues(carFilterValues)
    }

    //TODO: Populate the filter
    function createFilter(): void {
        const filter: CarFilter = {};
        filter.cities = selectedCities;
        if (isYearFilterEnabled) {
            filter.minYear = minYear;
            filter.maxYear = maxYear;
        }
        if (isPriceFilterEnabled) {
            filter.minPrice = minPrice;
            filter.maxPrice = maxPrice;
        }
        if (isMileageFilterEnabled) {
            filter.minMileage = minMileage;
            filter.maxMileage = maxMileage;
        }
        onApplyFilter(filter);
    }

    return (
        <>
            <Flex vertical>
                <Flex
                    wrap
                    justify="space-between"
                    align="flex-start"
                    gap='small'
                    style={{
                        width: '100%'
                    }}>
                    <Flex vertical style={{ maxWidth: '100%' }} gap='small'>
                        <Segmented
                            options={filterOptions}
                            value={selectedFilter}
                            onChange={setSelectedFilter}
                            style={{
                                width: 'auto',
                                overflow: 'scroll',
                                scrollbarWidth: 'none',
                            }}
                        />
                        <Flex style={{ padding: 8 }}>
                            {
                                selectedFilter == 'City' && <CityFilter
                                    allCities={carFilterValues?.cities ?? []}
                                    isCityFilterEnabled={isCityFilterEnabled}
                                    setCityFilterEnabled={setCityFilterEnabled}
                                    selectedCities={selectedCities}
                                    setSelectedCities={setSelectedCities}
                                />
                            }
                            {
                                selectedFilter == 'Car' && <CarFilter />
                            }
                            {
                                selectedFilter == 'Year' && <SliderFilter
                                    isFilterEnabled={isYearFilterEnabled}
                                    setFilterEnabled={setYearFilterEnabled}
                                    lowerLimit={carFilterValues?.minYear ?? 0}
                                    minValue={minYear}
                                    setMinValue={setMinYear}
                                    higherLimit={carFilterValues?.maxYear ?? 0}
                                    maxValue={maxYear}
                                    setMaxValue={setMaxYear}
                                />
                            }
                            {
                                selectedFilter == 'Price' && <SliderFilter
                                    isFilterEnabled={isPriceFilterEnabled}
                                    setFilterEnabled={setPriceFilterEnabled}
                                    lowerLimit={carFilterValues?.minPrice ?? 0}
                                    minValue={minPrice}
                                    setMinValue={setMinPrice}
                                    higherLimit={carFilterValues?.maxPrice ?? 0}
                                    maxValue={maxPrice}
                                    setMaxValue={setMaxPrice}
                                />
                            }
                            {
                                selectedFilter == 'Mileage' && <SliderFilter
                                    isFilterEnabled={isMileageFilterEnabled}
                                    setFilterEnabled={setMileageFilterEnabled}
                                    lowerLimit={carFilterValues?.minMileage ?? 0}
                                    minValue={minMileage}
                                    setMinValue={setMinMileage}
                                    higherLimit={carFilterValues?.maxMileage ?? 0}
                                    maxValue={maxMileage}
                                    setMaxValue={setMaxMileage}
                                />
                            }
                            {
                                selectedFilter == 'Power' && <PowerFilter />
                            }
                            {
                                selectedFilter == 'Torque' && <TorqueFilter />
                            }
                            {
                                selectedFilter == 'Length' && <LengthFilter />
                            }
                            {
                                selectedFilter == 'Width' && <WidthFilter />
                            }
                            {
                                selectedFilter == 'Height' && <HeightFilter />
                            }
                            {
                                selectedFilter == 'Wheelbase' && <WheelbaseFilter />
                            }
                        </Flex>
                    </Flex>
                    <Button onClick={e => createFilter()}>Apply</Button>
                </Flex>
            </Flex>
        </>
    );
}

type MakeModels = {
    make: string,
    models: {
        name: string,
        variants: string[]
    }[]
}

type CarFilterResponse = {
    cities: string[],
    makeModels: MakeModels[]
    minYear: number,
    maxYear: number,
    minPrice: number,
    maxPrice: number,
    minMileage: number,
    maxMileage: number,
    minPower: number,
    maxPower: number,
    minTorque: number,
    maxTorque: number,
    minLength: number,
    maxLength: number,
    minWidth: number,
    maxWidth: number,
    minHeight: number,
    maxHeight: number,
    minWheelbase: number,
    maxWheelbase: number,
}

export type CarFilter = {
    limit?: number,
    skip?: number,
    cities?: string[],
    makes?: string[],
    models?: string[],
    variants?: string[],
    minYear?: number,
    maxYear?: number,
    minPrice?: number,
    maxPrice?: number,
    minMileage?: number,
    maxMileage?: number,
    minPower?: number,
    maxPower?: number,
    minTorque?: number,
    maxTorque?: number,
    minLength?: number,
    maxLength?: number,
    minWidth?: number,
    maxWidth?: number,
    minHeight?: number,
    maxHeight?: number,
    minWheelbase?: number,
    maxWheelbase?: number,
}