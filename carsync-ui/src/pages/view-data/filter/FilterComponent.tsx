/* eslint-disable @typescript-eslint/no-unused-vars */
import { Button, Flex, Segmented, Spin, Typography } from "antd";
import { SegmentedOptions } from "antd/es/segmented";
import { valueType } from "antd/es/statistic/utils";
import { useEffect, useState } from "react";
import { get } from "../../../service/api";
import { CarFilter } from "./CarFilter";
import CityFilter from "./CityFilter";
import { SliderFilter } from "./SliderFilter";
import { SortOptions } from "./SortOptions";

const { Text } = Typography;

type FilterProps = {
    onApplyFilter: (carFilter: CarFilter) => void
}

export function FilterSortComponent(filterProps: FilterProps) {
    const { onApplyFilter } = filterProps

    const [selectedFilter, setSelectedFilter] = useState<string | number>('Sort');
    const [carFilterValues, setCarFilterValues] = useState<CarFilterResponse>();

    const [isSortEnabled, setSortEnabled] = useState<boolean>(false);

    const [isCityFilterEnabled, setCityFilterEnabled] = useState<boolean>(false);
    const [selectedCities, setSelectedCities] = useState<string[]>([]);

    const [isCarFilterEnabled, setCarFilterEnabled] = useState<boolean>(false);
    const [allMakeModels, setAllMakeModels] = useState<MakeModels[]>([])
    const [selectedCars, setSelectedCars] = useState<string[]>([]);

    const [isYearFilterEnabled, setYearFilterEnabled] = useState<boolean>(false);
    const [minYear, setMinYear] = useState<number>(0);
    const [maxYear, setMaxYear] = useState<number>(0);

    const [isPriceFilterEnabled, setPriceFilterEnabled] = useState<boolean>(false);
    const [minPrice, setMinPrice] = useState<number>(0);
    const [maxPrice, setMaxPrice] = useState<number>(0);

    const [isMileageFilterEnabled, setMileageFilterEnabled] = useState<boolean>(false);
    const [minMileage, setMinMileage] = useState<number>(0);
    const [maxMileage, setMaxMileage] = useState<number>(0);

    const [isPowerFilterEnabled, setPowerFilterEnabled] = useState<boolean>(false);
    const [minPower, setMinPower] = useState<number>(0);
    const [maxPower, setMaxPower] = useState<number>(0);

    const [isTorqueFilterEnabled, setTorqueFilterEnabled] = useState<boolean>(false);
    const [minTorque, setMinTorque] = useState<number>(0);
    const [maxTorque, setMaxTorque] = useState<number>(0);

    const [isLengthFilterEnabled, setLengthFilterEnabled] = useState<boolean>(false);
    const [minLength, setMinLength] = useState<number>(0);
    const [maxLength, setMaxLength] = useState<number>(0);

    const [isWidthFilterEnabled, setWidthFilterEnabled] = useState<boolean>(false);
    const [minWidth, setMinWidth] = useState<number>(0);
    const [maxWidth, setMaxWidth] = useState<number>(0);

    const [isHeightFilterEnabled, setHeightFilterEnabled] = useState<boolean>(false);
    const [minHeight, setMinHeight] = useState<number>(0);
    const [maxHeight, setMaxHeight] = useState<number>(0);

    const [isWheelbaseFilterEnabled, setWheelbaseFilterEnabled] = useState<boolean>(false);
    const [minWheelbase, setMinWheelbase] = useState<number>(0);
    const [maxWheelbase, setMaxWheelbase] = useState<number>(0);

    const [isAnyFilterEnabled, setAnyFiltereEnabled] = useState<boolean>(false)
    const [isApplyEnabled, setIsApplyEnabled] = useState<boolean>(true)

    const filterOptions: SegmentedOptions<valueType> = [
        {
            label: (
                <Text disabled={selectedFilter != 'Sort' && !isSortEnabled}>Sort</Text>
            ),
            value: 'Sort'
        },
        {
            label: (
                <Text disabled={selectedFilter != 'City' && !isCityFilterEnabled}>City</Text>
            ),
            value: 'City'
        },
        {
            label: (
                <Text disabled={selectedFilter != 'Car' && !isCarFilterEnabled}>Car</Text>
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
                <Text disabled={selectedFilter != 'Power' && !isPowerFilterEnabled}>Power</Text>
            ),
            value: 'Power'
        },
        {
            label: (
                <Text disabled={selectedFilter != 'Torque' && !isTorqueFilterEnabled}>Torque</Text>
            ),
            value: 'Torque'
        },
        {
            label: (
                <Text disabled={selectedFilter != 'Length' && !isLengthFilterEnabled}>Length</Text>
            ),
            value: 'Length'
        },
        {
            label: (
                <Text disabled={selectedFilter != 'Width' && !isWidthFilterEnabled}>Width</Text>
            ),
            value: 'Width'
        },
        {
            label: (
                <Text disabled={selectedFilter != 'Height' && !isHeightFilterEnabled}>Height</Text>
            ),
            value: 'Height'
        },
        {
            label: (
                <Text disabled={selectedFilter != 'Wheelbase' && !isWheelbaseFilterEnabled}>Wheelbase</Text>
            ),
            value: 'Wheelbase'
        },
    ]

    useEffect(() => {
        loadFilters();
    }, []);

    useEffect(() => {
        setCarFilterEnabled(selectedCars.length != 0)
    }, [selectedCars])

    useEffect(() => {
        setAnyFiltereEnabled(
            isCityFilterEnabled ||
            isCarFilterEnabled ||
            isYearFilterEnabled ||
            isPriceFilterEnabled ||
            isMileageFilterEnabled ||
            isPowerFilterEnabled ||
            isTorqueFilterEnabled ||
            isLengthFilterEnabled ||
            isWidthFilterEnabled ||
            isHeightFilterEnabled ||
            isWheelbaseFilterEnabled
        )
    }, [
        isCityFilterEnabled,
        isCarFilterEnabled,
        isYearFilterEnabled,
        isPriceFilterEnabled,
        isMileageFilterEnabled,
        isPowerFilterEnabled,
        isTorqueFilterEnabled,
        isLengthFilterEnabled,
        isWidthFilterEnabled,
        isHeightFilterEnabled,
        isWheelbaseFilterEnabled
    ])

    useEffect(() => {
        setIsApplyEnabled(true)
    }, [
        isCityFilterEnabled,
        isCarFilterEnabled,
        isYearFilterEnabled,
        isPriceFilterEnabled,
        isMileageFilterEnabled,
        isPowerFilterEnabled,
        isTorqueFilterEnabled,
        isLengthFilterEnabled,
        isWidthFilterEnabled,
        isHeightFilterEnabled,
        isWheelbaseFilterEnabled,
        carFilterValues,
        selectedCities,
        selectedCars,
        minYear,
        maxYear,
        minPrice,
        maxPrice,
        minMileage,
        maxMileage,
        minPower,
        maxPower,
        minTorque,
        maxTorque,
        minLength,
        maxLength,
        minWidth,
        maxWidth,
        minHeight,
        maxHeight,
        minWheelbase,
        maxWheelbase
    ])

    useEffect(() => {
        setSelectedCities([]);

        setAllMakeModels(carFilterValues?.makeModels ?? [])

        setMinYear(carFilterValues?.minYear ?? 0)
        setMaxYear(carFilterValues?.maxYear ?? 0)

        setMinPrice(carFilterValues?.minPrice ?? 0)
        setMaxPrice(carFilterValues?.maxPrice ?? 0)

        setMinMileage(carFilterValues?.minMileage ?? 0)
        setMaxMileage(carFilterValues?.maxMileage ?? 0)

        setMinPower(carFilterValues?.minPower ?? 0)
        setMaxPower(carFilterValues?.maxPower ?? 0)

        setMinTorque(carFilterValues?.minTorque ?? 0)
        setMaxTorque(carFilterValues?.maxTorque ?? 0)

        setMinLength(carFilterValues?.minLength ?? 0)
        setMaxLength(carFilterValues?.maxLength ?? 0)

        setMinWidth(carFilterValues?.minWidth ?? 0)
        setMaxWidth(carFilterValues?.maxWidth ?? 0)

        setMinHeight(carFilterValues?.minHeight ?? 0)
        setMaxHeight(carFilterValues?.maxHeight ?? 0)

        setMinWheelbase(carFilterValues?.minWheelbase ?? 0)
        setMaxWheelbase(carFilterValues?.maxWheelbase ?? 0)

    }, [carFilterValues])

    function disableAllFilters() {
        setSelectedCities([])
        setSelectedCars([])
        setCarFilterEnabled(false);
        setYearFilterEnabled(false);
        setPriceFilterEnabled(false);
        setMileageFilterEnabled(false);
        setPowerFilterEnabled(false);
        setTorqueFilterEnabled(false);
        setLengthFilterEnabled(false);
        setWidthFilterEnabled(false);
        setHeightFilterEnabled(false);
        setWheelbaseFilterEnabled(false);
    }

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

        const carDetails = selectedCars.map(car => JSON.parse(car) as string[])
        const makes = new Set(carDetails.map(car => car[0]))
        const models = new Set(carDetails.map(car => car[1]))
        const variants = new Set(carDetails.map(car => car[2]))
        filter.makes = [...makes];
        filter.models = [...models];
        filter.variants = [...variants];

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
        if (isPowerFilterEnabled) {
            filter.minPower = minPower;
            filter.maxPower = maxPower;
        }
        if (isTorqueFilterEnabled) {
            filter.minTorque = minTorque;
            filter.maxTorque = maxTorque;
        }
        if (isLengthFilterEnabled) {
            filter.minLength = minLength;
            filter.maxLength = maxLength;
        }
        if (isWidthFilterEnabled) {
            filter.minWidth = minWidth;
            filter.maxWidth = maxWidth;
        }
        if (isHeightFilterEnabled) {
            filter.minHeight = minHeight;
            filter.maxHeight = maxHeight;
        }
        if (isWheelbaseFilterEnabled) {
            filter.minWheelbase = minWheelbase;
            filter.maxWheelbase = maxWheelbase;
        }
        onApplyFilter(filter);
        setIsApplyEnabled(false)
    }

    return (
        <>
            <Spin spinning={carFilterValues == undefined}>
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
                            <Flex style={{ padding: 0 }}>
                                {
                                    selectedFilter == 'Sort' && <SortOptions />
                                }
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
                                    selectedFilter == 'Car' && <CarFilter
                                        allMakeModels={allMakeModels}
                                        selectedCars={selectedCars}
                                        setSelectedCars={setSelectedCars}
                                    />
                                }
                                {
                                    selectedFilter == 'Year' && <SliderFilter
                                        name={selectedFilter}
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
                                        name={selectedFilter}
                                        unit="₹"
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
                                        name={selectedFilter}
                                        unit="km"
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
                                    selectedFilter == 'Power' && <SliderFilter
                                        name={selectedFilter}
                                        unit="bhp"
                                        isFilterEnabled={isPowerFilterEnabled}
                                        setFilterEnabled={setPowerFilterEnabled}
                                        lowerLimit={carFilterValues?.minPower ?? 0}
                                        minValue={minPower}
                                        setMinValue={setMinPower}
                                        higherLimit={carFilterValues?.maxPower ?? 0}
                                        maxValue={maxPower}
                                        setMaxValue={setMaxPower}
                                    />
                                }
                                {
                                    selectedFilter == 'Torque' && <SliderFilter
                                        name={selectedFilter}
                                        unit="nm"
                                        isFilterEnabled={isTorqueFilterEnabled}
                                        setFilterEnabled={setTorqueFilterEnabled}
                                        lowerLimit={carFilterValues?.minTorque ?? 0}
                                        minValue={minTorque}
                                        setMinValue={setMinTorque}
                                        higherLimit={carFilterValues?.maxTorque ?? 0}
                                        maxValue={maxTorque}
                                        setMaxValue={setMaxTorque}
                                    />
                                }
                                {
                                    selectedFilter == 'Length' && <SliderFilter
                                        name={selectedFilter}
                                        unit="mm"
                                        isFilterEnabled={isLengthFilterEnabled}
                                        setFilterEnabled={setLengthFilterEnabled}
                                        lowerLimit={carFilterValues?.minLength ?? 0}
                                        minValue={minLength}
                                        setMinValue={setMinLength}
                                        higherLimit={carFilterValues?.maxLength ?? 0}
                                        maxValue={maxLength}
                                        setMaxValue={setMaxLength}
                                    />
                                }
                                {
                                    selectedFilter == 'Width' && <SliderFilter
                                        name={selectedFilter}
                                        unit="mm"
                                        isFilterEnabled={isWidthFilterEnabled}
                                        setFilterEnabled={setWidthFilterEnabled}
                                        lowerLimit={carFilterValues?.minWidth ?? 0}
                                        minValue={minWidth}
                                        setMinValue={setMinWidth}
                                        higherLimit={carFilterValues?.maxWidth ?? 0}
                                        maxValue={maxWidth}
                                        setMaxValue={setMaxWidth}
                                    />
                                }
                                {
                                    selectedFilter == 'Height' && <SliderFilter
                                        name={selectedFilter}
                                        unit="mm"
                                        isFilterEnabled={isHeightFilterEnabled}
                                        setFilterEnabled={setHeightFilterEnabled}
                                        lowerLimit={carFilterValues?.minHeight ?? 0}
                                        minValue={minHeight}
                                        setMinValue={setMinHeight}
                                        higherLimit={carFilterValues?.maxHeight ?? 0}
                                        maxValue={maxHeight}
                                        setMaxValue={setMaxHeight}
                                    />
                                }
                                {
                                    selectedFilter == 'Wheelbase' && <SliderFilter
                                        name={selectedFilter}
                                        unit="mm"
                                        isFilterEnabled={isWheelbaseFilterEnabled}
                                        setFilterEnabled={setWheelbaseFilterEnabled}
                                        lowerLimit={carFilterValues?.minWheelbase ?? 0}
                                        minValue={minWheelbase}
                                        setMinValue={setMinWheelbase}
                                        higherLimit={carFilterValues?.maxWheelbase ?? 0}
                                        maxValue={maxWheelbase}
                                        setMaxValue={setMaxWheelbase}
                                    />
                                }
                            </Flex>
                        </Flex>
                        <Flex gap='small'>
                            {isAnyFilterEnabled && <Button onClick={() => disableAllFilters()}>Reset filters</Button>}
                            <Button type="primary" disabled={!isApplyEnabled} onClick={() => createFilter()}>Apply</Button>
                        </Flex>
                    </Flex>
                </Flex>
            </Spin>
        </>
    );
}

export type MakeModels = {
    make: string,
    models: {
        name: string,
        variants: {
            name: string
        }[]
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