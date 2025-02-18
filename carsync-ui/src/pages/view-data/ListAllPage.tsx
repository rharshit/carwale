import { Flex, Typography } from 'antd';
import React, { useEffect, useState } from 'react';
import { post } from '../../service/api';
import { CarFilter, FilterComponent } from './filter/FilterComponent';

const { Title } = Typography;

const ListAllPage: React.FC = () => {

    const [carFilter, setCarFilter] = useState<CarFilter>()

    useEffect(() => {
        fetchCars(carFilter)
    }, [carFilter])

    async function fetchCars(carFilter: CarFilter | undefined) {
        if (!carFilter) {
            return;
        }
        console.log('Fetching cars', carFilter)
        Promise.resolve(post('/car', carFilter).then(res => {
            console.log('data', res)
        }))
    }

    const onApplyFilter = (carFilter: CarFilter) => {
        setCarFilter(carFilter)
    }

    return (
        <>
            <Flex vertical>
                <Title level={3}>
                    All Cars
                </Title>
                <FilterComponent
                    onApplyFilter={onApplyFilter}
                />
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