import { Flex, Typography, theme } from 'antd';
import React, { useState } from 'react';
import { post } from '../../../service/api';
import { CarFilter, FilterSortComponent } from '../filter/FilterComponent';

const { Title } = Typography;

const ListAllPage: React.FC = () => {

    const {
        token: { colorBgContainer },
    } = theme.useToken();

    const [isApplyingFilter, setApplyingFilter] = useState<boolean>(false)
    const [isFilterApplied, setFilterApplied] = useState<boolean>(false)

    async function fetchCars(carFilter: CarFilter, setApplyingFilter: React.Dispatch<React.SetStateAction<boolean>>, setFilterApplied: React.Dispatch<React.SetStateAction<boolean>>) {
        if (!carFilter) {
            setApplyingFilter(false)
            setFilterApplied(false)
            return;
        }
        console.log('Fetching cars', carFilter)
        Promise.resolve(
            post('/car', carFilter)
                .then(res => {
                    console.log('data', res)
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
                <p>long content</p>
                {
                    // indicates very long content
                    Array.from({ length: 100 }, (_, index) => (
                        <React.Fragment key={index}>
                            {index % 20 === 0 && index ? 'more' : '...'}
                            <br />
                        </React.Fragment>
                    ))
                }
                <p>end</p>
            </Flex>
        </>
    );
}

export default ListAllPage;