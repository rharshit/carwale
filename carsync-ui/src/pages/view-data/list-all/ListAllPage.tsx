import { Flex, Typography, theme } from 'antd';
import React, { useState } from 'react';
import { post } from '../../../service/api';
import { CarModel } from '../common/Types';
import { CarFilter, FilterSortComponent } from '../filter/FilterComponent';
import AllCarsList from './AllCarsList';

const { Title } = Typography;

export interface AllCarsResponse {
    success: boolean
    error: string
    total: number
    length: number
    cars: CarModel[]
    loadMore: boolean
}

const ListAllPage: React.FC = () => {

    const {
        token: { colorBgContainer },
    } = theme.useToken();

    const [isApplyingFilter, setApplyingFilter] = useState<boolean>(false)
    const [isFilterApplied, setFilterApplied] = useState<boolean>(false)

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
                    zIndex: 1,
                    width: '100%',
                    display: 'flex',
                    background: colorBgContainer,
                }}>
                    <Title level={3}>
                        All Cars
                    </Title>
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
                />
            </Flex>
        </>
    );
}

export default ListAllPage;