import React, { useEffect, useState } from "react";
import { Dropdown, DropdownButton } from "react-bootstrap";
import DropdownMenu from "react-bootstrap/esm/DropdownMenu";
import { LineChart, Line, CartesianGrid, XAxis, YAxis, ResponsiveContainer, Legend, Tooltip } from "recharts";

const url = "http://192.168.29.246:8080";

const Graph = () => {
    
    const [graphData, setGraphData] = useState<any[]>([]);

    const [pending, setPending] = useState(true);

    const [dataMin, setDataMin] = useState(0);
    const [dataMax, setDataMax] = useState(0);

    const getGraphData = async() => {
        const response = await fetch(`${url}/allgraph`);
        const data = await response.json();

        setGraphData(data);

        var currentMin: number = Number.MAX_SAFE_INTEGER;
        var currentMax: number = Number.MIN_SAFE_INTEGER;

        data.map((dp: {name: string, uv: number, close: number;}) => {
            currentMin = Math.min(currentMin, dp.close);
            currentMax = Math.max(currentMin, dp.close);
        })

        currentMin = Math.round(currentMin * 100) / 100;
        currentMax = Math.round(currentMax * 100) / 100;

        setDataMax(currentMax);
        setDataMin(currentMin);

        setPending(false); 

    }

    useEffect(() => {
        getGraphData();
    }, [])

    return (
        <>
            <h3>My Total Investments</h3>
            <br />     
            {
                pending ? <p>Loading data</p>  : 
                <div>
                    <br />
                    <LineChart
                        width={1200}
                        height={500}
                        data={graphData}
                        margin={{
                            top: 5,
                            right: 30,
                            left: 20,
                            bottom: 5,
                        }}
                        >
                        <Tooltip />
                        <CartesianGrid strokeDasharray="3 3" />
                        <XAxis dataKey="name" />
                        <YAxis domain={[Math.round((dataMin - dataMin / 3) * 100) / 100, dataMax + 1000]} />
                        <Line type="monotone" dataKey="investment" stroke="green" activeDot={{ r: 5 }} />
                        <Legend />
                    </LineChart>  
                </div>              
            }
     </>
    );

}

export default Graph;