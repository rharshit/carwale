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
import MileageFilter from "./MileageFilter";
import { default as PowerFilter, default as PriceFilter } from "./PowerFilter";
import TorqueFilter from "./TorqueFilter";
import WheelbaseFilter from "./WheelbaseFilter";
import WidthFilter from "./WidthFilter";
import YearFilter from "./YearFilter";

const { Title, Paragraph, Text, Link } = Typography;

type FilterProps = {
    onApplyFilter: (carFilter: CarFilter) => void
}

const filterOptions: SegmentedOptions<valueType> = [
    {
        label: (
            <Text>City</Text>
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
            <Text>Year</Text>
        ),
        value: 'Year'
    },
    {
        label: (
            <Text>Price</Text>
        ),
        value: 'Price'
    },
    {
        label: (
            <Text>Mileage</Text>
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

export function FilterComponent(filterProps: FilterProps) {
    const { onApplyFilter } = filterProps
    const [selectedFilter, setSelectedFilter] = useState<string | number>();
    const [carFilterValues, setCarFilterValues] = useState<CarFilterResponse>();

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
        const filter: CarFilter = {}
        onApplyFilter(filter)
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
                    <Flex vertical style={{ maxWidth: '100%' }}>
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
                        <Flex>
                            {
                                selectedFilter == 'City' && <CityFilter />
                            }
                            {
                                selectedFilter == 'Car' && <CarFilter />
                            }
                            {
                                selectedFilter == 'Year' && <YearFilter />
                            }
                            {
                                selectedFilter == 'Price' && <PriceFilter />
                            }
                            {
                                selectedFilter == 'Mileage' && <MileageFilter />
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