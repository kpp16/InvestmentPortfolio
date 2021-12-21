import React, { useEffect, useState } from "react";
import 'bootstrap/dist/css/bootstrap.min.css';
import { Button, Modal, Table } from "react-bootstrap";
import {AiTwotoneEdit, AiTwotoneDelete} from "react-icons/ai";
import { GoPlus } from "react-icons/go";
import Graph from "./Graph";

const fetchURL = "http://192.168.29.246:8080";

interface IInvestment {
    quote: string, 
    quantity: number,
    price: number,
    buyDate: string,
    change: number,
    id: number
};

const AllInvestmentData = () => {

    const [investments, setInvestments] = useState<IInvestment[]>([]);
    const [show, setShow] = useState(false);
    const [quote, setQuote] = useState("");
    const [quantity, setQuantity] = useState(0);
    const [buyDate, setBuyDate] = useState("");
    const [showLoading, setShowLoading] = useState(false);
    const [addInvestmentResponse, setAddInvestmentResponse] = useState("");
    const [showEdit, setShowEdit] = useState(false);
    const [id, setID] = useState(0);
    const [showEditing, setShowEdititng] = useState(false);

    const handleClose = () => setShow(false);
    const handleShow = () => setShow(true);
    const handleCloseEdit = () => setShowEdit(false);
    const handleShowEdit = (invid: number) => {
        setID(invid);
        setShowEdit(true);
    }

    var totalInvestments = 0;

    var refreshData = 0;    

    const postData = async() => {
        let requestOptions = {}
        if (buyDate == "") {
            requestOptions = {
                method: "POST",
                headers: {'Content-Type': 'application/json'},
                body: JSON.stringify({"quote": quote, "quantity": quantity})
            }
        } else {
            requestOptions = {
                method: "POST",
                headers: {'Content-Type': 'application/json'},
                body: JSON.stringify({"quote": quote, "quantity": quantity, "buyDate": buyDate})
            }            
        }

        const response = await fetch(`${fetchURL}/addinvestment`, requestOptions)
        return response;
    }

    const handleInvestment = async() => {
        setShowLoading(true);
        const resp = await postData();

        if (refreshData === 0) {
            refreshData = 1;
        } else {
            refreshData = 0;
        }
        
        if (resp.status === 200) {
            setShowLoading(false);
            setShow(false);
            window.location.reload();
        }
    }

    const handleEdit = async() => {
        setShowEdititng(true);
        const options = {
            method: "POST",
            headers: {'Content-Type': 'application/json'},
            body: JSON.stringify({"buyDate": buyDate, "quantity": quantity, "id": id})
        };
        const response = await fetch(`${fetchURL}/updateinvestment`, options);
        setShowEdititng(false);
        window.location.reload();

    }


    const fetchData = async() => {
        const response = await fetch(`${fetchURL}/investments`)
        const data = await response.json();
        setInvestments(data);
    }

    useEffect(() => {
        fetchData();
    }, [])

    const deleteInvestment = async(id: number) => {
        const requestOptions = {
            method: "DELETE"
        }
        const response = await fetch(`${fetchURL}/delete/${id.toString()}`, requestOptions)
        window.location.reload();
    }

    return (
        <div className="body">
            <h3>My Investment Portfolio</h3>
            <br />
            <Button variant="primary" onClick={handleShow}>
                <GoPlus />
                Add Investment
            </Button>
            <Modal show={show} onHide={handleClose}>
                <Modal.Header closeButton>
                <Modal.Title>Add Investment</Modal.Title>
                </Modal.Header>
                <Modal.Body>
                    <p className="addInvestment">
                        <p>
                            Quote: <input type="text" onChange={e => setQuote(e.target.value)} />
                        </p>
                        <p>
                            Qunatity: <input type="number" onChange={e => setQuantity(e.target.valueAsNumber)} />
                        </p>
                        <p>
                            Buy Date: <input type="date" onChange={e => setBuyDate(e.target.value)}/>
                        </p>
                    </p>
                    <br />
                    <p>{showLoading === true ? "Adding Investment. Please wait" : ""}</p>
                </Modal.Body>
                <Modal.Footer>
                <Button variant="secondary" onClick={handleClose}>
                    Close
                </Button>
                <Button variant="primary" onClick={handleInvestment}>
                    Add investemnt
                </Button>
                </Modal.Footer>
            </Modal>
            <Modal show={showEdit} onHide={handleCloseEdit}>
                <Modal.Header closeButton>
                <Modal.Title>Edit Investment</Modal.Title>
                </Modal.Header>
                <Modal.Body>
                    <p className="addInvestment">
                        <p>
                            Qunatity: <input type="number" onChange={e => setQuantity(e.target.valueAsNumber)} />
                        </p>
                        <p>
                            Buy Date: <input type="date" onChange={e => setBuyDate(e.target.value)}/>
                        </p>
                    </p>
                    <br />
                    <p>{showEditing === true ? "Editing Investment. Please wait." : ""}</p>
                </Modal.Body>
                <Modal.Footer>
                <Button variant="secondary" onClick={handleCloseEdit}>
                    Close
                </Button>
                <Button variant="primary" onClick={handleEdit}>
                    Edit investemnt
                </Button>
                </Modal.Footer>
            </Modal>    

            {investments.map((investment) => {
                totalInvestments += investment.quantity * investment.price
            })}
            
            <div>
                <br />
                <Table responsive>
                    <thead>
                        <tr>
                            <th>Quote</th>
                            <th>Quantity</th>
                            <th>Current Stock Price</th>
                            <th>Investment Value</th>
                            <th>Buy Date</th>
                            <th>Change</th>
                            <th>Edit Investment</th>
                            <th>Delete Investment</th>
                        </tr>
                    </thead>
                    <tbody>
                        {investments.map((investment) => (
                            <tr className = "extraFontWeight">
                                <td>
                                    {investment.quote} 
                                </td>
                                <td>
                                    {investment.quantity} 
                                </td>
                                <td>
                                    ${investment.price} 
                                </td>
                                <td>
                                    <p className = "investmentValue" >${investment.quantity * investment.price}</p>
                                </td>
                                <td>
                                    {investment.buyDate} 
                                </td>
                                <td>
                                    <p className = {investment.change >= 0 ? "green" : "red"}>{investment.change}%</p>
                                </td>
                                <td>
                                    <Button variant="success" onClick={() => handleShowEdit(investment.id)}>
                                        <AiTwotoneEdit />
                                    </Button>
                                </td>
                                <td>
                                    <Button variant="danger" onClick={() => deleteInvestment(investment.id)}>
                                        <AiTwotoneDelete />
                                    </Button>
                                </td>
                            </tr>
                        ))}
                    </tbody>
                </Table>
                <p className="totalInvestment">
                    Total Investment: ${totalInvestments}
                </p>
            </div>
            <br />
            <Graph />
        </div>
    );

}

export default AllInvestmentData;