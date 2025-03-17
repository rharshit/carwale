import { Flex, TableProps, theme } from 'antd';
import React, { useState } from 'react';
import { post } from '../../../service/api';
import { CarModel } from '../common/Types';
import { CarFilter, FilterSortComponent } from '../filter/FilterComponent';
import AllCarsList from './AllCarsList';

export interface AllCarsResponse {
    success: boolean
    error: string
    total: number
    length: number
    cars: CarModel[]
    loadMore: boolean
}

export type TableCarData = {
    key: React.ReactNode,
    name: string,
    city?: string,
    make?: string,
    model?: string,
    variant?: string,
    url?: string,
    imageUrls?: string[],
    year: (number | null)[],
    price: (number | null)[],
    mileage: (number | null)[],
    power: (number | null)[],
    torque: (number | null)[],
    displacement: (number | null)[],
    length: (number | null)[],
    width: (number | null)[],
    height: (number | null)[],
    wheelbase: (number | null)[],
    children?: TableCarData[]
}

type GetSingle<T> = T extends (infer U)[] ? U : never;
export type TableCarSort = GetSingle<Parameters<NonNullable<TableProps<TableCarData>['onChange']>>[2]>;

const ListAllPage: React.FC = () => {

    const {
        token: { colorBgContainer },
    } = theme.useToken();

    const [isApplyingFilter, setApplyingFilter] = useState<boolean>(false)
    const [isFilterApplied, setFilterApplied] = useState<boolean>(false)
    const [sortedInfo, setSortedInfo] = useState<TableCarSort>({});

    const [allCars, setAllCars] = useState<CarModel[]>([])

    async function fetchCars(carFilter: CarFilter, setApplyingFilter: React.Dispatch<React.SetStateAction<boolean>>, setFilterApplied: React.Dispatch<React.SetStateAction<boolean>>) {
        if (!carFilter) {
            setApplyingFilter(false)
            setFilterApplied(false)
            return;
        }
        Promise.resolve(
            post('/car', carFilter)
                .then(res => {
                    const allCarsResponse: AllCarsResponse = res as AllCarsResponse;
                    setAllCars(allCarsResponse.cars ?? [])
                    setFilterApplied(true)
                })
                .catch(e => {
                    console.error('Error fetching list of cars', e)
                    setFilterApplied(false)
                })
                .finally(() => {
                    setApplyingFilter(false)
                })
        )
    }

    const onApplyFilter = (
        carFilter: CarFilter,
        setApplyingFilter: React.Dispatch<React.SetStateAction<boolean>>,
        setFilterApplied: React.Dispatch<React.SetStateAction<boolean>>
    ) => {
        fetchCars(carFilter, setApplyingFilter, setFilterApplied)
    }

    return (
        <>
            <Flex vertical>
                <Flex vertical style={{
                    position: 'sticky',
                    top: 0,
                    zIndex: 5,
                    width: '100%',
                    display: 'flex',
                    background: colorBgContainer,
                }}>
                    <FilterSortComponent
                        onApplyFilter={onApplyFilter}
                        isApplyingFilter={isApplyingFilter}
                        setApplyingFilter={setApplyingFilter}
                        isFilterApplied={isFilterApplied}
                        setFilterApplied={setFilterApplied}
                    />
                </Flex>
                <AllCarsList
                    allCars={allCars}
                    loading={isApplyingFilter}
                    sortedInfo={sortedInfo}
                    setSortedInfo={setSortedInfo}
                />
            </Flex>
        </>
    );
}

export default ListAllPage;