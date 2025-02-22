import { Flex, Input, Tag } from "antd";
import { useEffect, useState } from "react";

type CityFilterProps = {
    allCities: string[],
    isCityFilterEnabled: boolean,
    setCityFilterEnabled: React.Dispatch<React.SetStateAction<boolean>>,
    selectedCities: string[],
    setSelectedCities: React.Dispatch<React.SetStateAction<string[]>>
}

export function CityFilter(cityFilterProps: CityFilterProps) {
    const {
        allCities,
        isCityFilterEnabled,
        setCityFilterEnabled,
        selectedCities,
        setSelectedCities
    } = cityFilterProps;

    const [filteredCities, setFilteredCities] = useState<string[]>([])
    const [searchValue, setSearchValue] = useState<string>('')

    const handleChange = (city: string, checked: boolean) => {
        const nextSelectedCities = checked
            ? [...selectedCities, city]
            : selectedCities.filter((t) => t !== city);
        setSelectedCities(nextSelectedCities);
    };

    const onSearch = (e: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement>) => {
        const value = e.target.value;
        setSearchValue(value);
    };

    useEffect(() => {
        if (searchValue == '') {
            setFilteredCities(allCities)
        } else {
            setFilteredCities(allCities.filter(city => city.toUpperCase().startsWith(searchValue.toUpperCase())))
        }
    }, [searchValue, allCities])

    useEffect(() => {
        if (selectedCities.length == 0) {
            setCityFilterEnabled(false)
        } else {
            setCityFilterEnabled(true)
        }
    }, [selectedCities])

    return <>
        <Flex vertical gap='small' style={{ padding: 0 }}>
            <Input placeholder="Search" value={searchValue} allowClear onChange={onSearch} />
            <Flex wrap style={{ maxHeight: 200, maxWidth: 690, overflow: 'scroll' }}>
                {isCityFilterEnabled && <Tag
                    closeIcon
                    onClose={e => setSelectedCities([])}
                    style={{ margin: 2 }}
                >
                    Deselect all
                </Tag>}
                {selectedCities.map<React.ReactNode>((city) => (
                    <Tag.CheckableTag
                        key={city}
                        checked={true}
                        onChange={(checked) => handleChange(city, checked)}
                        style={{ margin: 2 }}
                    >
                        {city}
                    </Tag.CheckableTag>
                ))}
                {filteredCities.filter(city => !selectedCities.includes(city)).map<React.ReactNode>((city) => (
                    <Tag.CheckableTag
                        key={city}
                        checked={false}
                        onChange={(checked) => handleChange(city, checked)}
                        style={{ margin: 2 }}
                    >
                        {city}
                    </Tag.CheckableTag>
                ))}
            </Flex>
        </Flex>
    </>
}

export default CityFilter;